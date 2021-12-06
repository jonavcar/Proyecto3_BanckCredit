package com.banck.banckcredit.infraestructure.rest;

import com.banck.banckcredit.domain.Product;
import com.banck.banckcredit.domain.ProductSummaryDto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.banck.banckcredit.aplication.impl.ProductOperationsImpl;
import com.banck.banckcredit.utils.ProductType;
import com.banck.banckcredit.utils.CustomerType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.banck.banckcredit.utils.SunatUtils;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import com.banck.banckcredit.aplication.ProductOperations;
import com.banck.banckcredit.aplication.ScheduleOperations;
import org.springframework.http.HttpStatus;

/**
 *
 * @author jonavcar
 */
@RestController
@RequestMapping("/banck-credit")
@RequiredArgsConstructor
public class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductOperationsImpl.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/Bogota"));
    private final ProductOperations operations;
    private final ScheduleOperations scheduleOperations;

    @GetMapping
    public Flux<Product> listAll() {
        return operations.list();
    }

    @GetMapping("/{id}")
    public Mono<Product> get(@PathVariable("id") String id) {
        return operations.get(id);
    }

    @GetMapping("/{customer}/list")
    public Flux<Product> listAccountByCustomer(@PathVariable("customer") String customer) {
        return operations.listByCustomer(customer);
    }

    @GetMapping("/summary/{customer}/list")
    public Flux<ProductSummaryDto> listProductSummaryByCustomer(@PathVariable("customer") String customer) {
        return operations.listProductSummaryByCustomer(customer);
    }

    @PostMapping
    public Mono<ResponseEntity> create(@RequestBody Product c) {
        c.setStatus(true);
        c.setDate(dateTime.format(formatter));
        return Mono.just(c).flatMap(m -> {

            if (Optional.ofNullable(m.getCustomer()).isEmpty()) {
                return Mono.just(ResponseEntity.ok("Debe ingresar el cliente, Ejemplo { \"customer\": \"78345212\" }"));
            }

            return scheduleOperations.isupToDate(m.getCustomer()).flatMap(isup -> {
                if (isup) {
                    String msgTipoProducto
                            = "Credito Personal = { \"productType\": \"CP\" }\n"
                            + "Credito Empresarial = { \"productType\": \"CE\" }\n"
                            + "Targeta Debito = { \"productType\": \"TD\" }\n"
                            + "Targeta Credito = { \"productType\": \"TC\" }";
                    String msgTipoCliente
                            = "Cliente Personal = { \"customerType\": \"CP\" }\n"
                            + "Cliente Personal VIP = { \"customerType\": \"CPV\" }\n"
                            + "Cliente Empresarial = { \"customerType\": \"CE\" }\n"
                            + "Cliente Empresarial PYME = { \"customerType\": \"CEP\" }";

                    if (Optional.ofNullable(m.getProductType()).isEmpty()) {
                        return Mono.just(ResponseEntity.ok(""
                                + "Debe ingresar Tipo Producto, Ejemplos: \n"
                                + msgTipoProducto));
                    }

                    if (Optional.ofNullable(m.getCustomerType()).isEmpty()) {
                        return Mono.just(ResponseEntity.ok(""
                                + "Debe ingresar Tipo Cliente, Ejemplos: \n"
                                + msgTipoCliente));
                    }


                    /*
                Se realiza validacion basica de datos para poder registrar
                un credito bancario
                     */
                    boolean isCreditType = false;
                    for (ProductType tc : ProductType.values()) {
                        if (m.getProductType().equals(tc.value)) {
                            isCreditType = true;
                        }
                    }

                    boolean isCustomerType = false;
                    for (CustomerType tc : CustomerType.values()) {
                        if (m.getCustomerType().equals(tc.value)) {
                            isCustomerType = true;
                        }
                    }
                    if (!isCreditType || Optional.ofNullable(m.getProductType()).isEmpty()) {
                        return Mono.just(ResponseEntity.ok(""
                                + "Solo existen estos Codigos de Productos: \n"
                                + msgTipoProducto));
                    }
                    if (!isCustomerType || Optional.ofNullable(m.getCustomerType()).isEmpty()) {
                        return Mono.just(ResponseEntity.ok(""
                                + "Solo existen estos Codigos de Tipos de Clientes: \n"
                                + msgTipoCliente));
                    }

                    c.setProduct(c.getProductType() + "-" + c.getCustomer() + "-" + getRandomNumberString());

                    if (CustomerType.PERSONAL.equals(m.getCustomerType())
                            || CustomerType.PERSONAL_VIP.equals(m.getCustomerType())) {

                        if (SunatUtils.isRUCValid(m.getCustomer())) {
                            return Mono.just(ResponseEntity.ok("El RUC " + m.getCustomer() + " es solo para empresas, debe registrarse con DNI."));
                        }

                    } else {
                        if (!SunatUtils.isRUCValid(m.getCustomer())) {
                            return Mono.just(ResponseEntity.ok("El RUC " + m.getCustomer() + " de la Empresa No es Válido!!"));
                        }
                    }

                    /*
                Se define la logica de los creditos al registra un credito segun
                el tipo de credito que se esta creando
                     */
                    if (CustomerType.PERSONAL.equals(m.getCustomerType()) || CustomerType.PERSONAL_VIP.equals(m.getCustomerType())) {

                        return operations.listByCustomer(m.getCustomer()).filter(p -> p.getProductType().equals(m.getProductType())).count().flatMap(fm -> {
                            if (ProductType.CREDIT_CARD.equals(m.getProductType())) {
                                return operations.create(c).flatMap(rp -> {
                                    return Mono.just(ResponseEntity.ok(rp));
                                });
                            } else if (ProductType.BUSINESS_CREDIT.equals(m.getProductType())) {
                                return Mono.just(ResponseEntity.ok("Usted no puede tener credito empresarial"));
                            } else {
                                if (fm.intValue() == 0) {
                                    return operations.create(c).flatMap(rp -> {
                                        return Mono.just(ResponseEntity.ok(rp));
                                    });
                                } else {
                                    return Mono.just(ResponseEntity.ok("Usted solo puede tener un credito personal"));
                                }
                            }

                        });
                    } else {
                        if (ProductType.PERSONAL_CREDIT.equals(m.getProductType())) {
                            return Mono.just(ResponseEntity.ok("Usted no puede tener credito personal!!"));
                        } else {
                            return operations.create(c).flatMap(rp -> {
                                return Mono.just(ResponseEntity.ok(rp));
                            });
                        }
                    }

                } else {
                    return Mono.just(new ResponseEntity("La persona " + m.getCustomer() + " Tiene deudas vencidas por lo que no puede realizar estas operaciones", HttpStatus.BAD_REQUEST));
                }
            });
        });
    }

    @PutMapping("/{id}")
    public Mono<Product> update(@PathVariable("id") String id, @RequestBody Product c) {
        return operations.update(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        operations.delete(id);
    }

    @GetMapping("/card/{customer}/count")
    public Mono<Integer> countCardByCustomer(@PathVariable("customer") String customer) {
        return operations.countCardByCustomer(customer);
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(99);
        return String.format("%02d", number);
    }
}
