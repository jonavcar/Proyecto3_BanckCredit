package com.banck.banckcredit.infraestructure.rest;

import com.banck.banckcredit.domain.Credit;
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
import com.banck.banckcredit.aplication.impl.CreditOperationsImpl;
import com.banck.banckcredit.utils.CreditType;
import com.banck.banckcredit.utils.CustomerType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.banck.banckcredit.aplication.CreditOperations;
import com.banck.banckcredit.utils.SunatUtils;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author jonavcar
 */
@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditController {

    Logger logger = LoggerFactory.getLogger(CreditOperationsImpl.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/Bogota"));
    private final CreditOperations operations;

    @GetMapping
    public Flux<Credit> listAll() {
        return operations.list();
    }

    @GetMapping("/{id}")
    public Mono<Credit> get(@PathVariable("id") String id) {
        return operations.get(id);
    }

    @GetMapping("/{customer}/list")
    public Flux<Credit> listAccountByCustomer(@PathVariable("customer") String customer) {
        return operations.listByCustomer(customer);
    }

    @PostMapping
    public Mono<ResponseEntity> create(@RequestBody Credit c) {
        c.setCredit(c.getCustomer() + "-" + getRandomNumberString());
        c.setDateCreated(dateTime.format(formatter));

        return Mono.just(c).flatMap(m -> {
            String msgTipoCredito
                    = "Credito Personal = { \"creditType\": \"CP\" }\n"
                    + "Credito Empresarial = { \"creditType\": \"CE\" }\n"
                    + "Targeta Credito = { \"creditType\": \"TC\" }";
            String msgTipoCliente
                    = "Cliente Personal = { \"customerType\": \"CP\" }\n"
                    + "Cliente Personal VIP = { \"customerType\": \"CPV\" }\n"
                    + "Cliente Empresarial = { \"customerType\": \"CE\" }\n"
                    + "Cliente Empresarial PYME = { \"customerType\": \"CEP\" }";

            if (Optional.ofNullable(m.getCreditType()).isEmpty()) {
                return Mono.just(ResponseEntity.ok(""
                        + "Debe ingresar Tipo Credito, Ejemplos: \n"
                        + msgTipoCredito));
            }

            if (Optional.ofNullable(m.getCustomer()).isEmpty()) {
                return Mono.just(ResponseEntity.ok("Debe ingresar el cliente, Ejemplo { \"customer\": \"78345212\" }"));
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
            for (CreditType tc : CreditType.values()) {
                if (m.getCreditType().equals(tc.value)) {
                    isCreditType = true;
                }
            }

            boolean isCustomerType = false;
            for (CustomerType tc : CustomerType.values()) {
                if (m.getCustomerType().equals(tc.value)) {
                    isCustomerType = true;
                }
            }
            if (!isCreditType || Optional.ofNullable(m.getCreditType()).isEmpty()) {
                return Mono.just(ResponseEntity.ok(""
                        + "Solo existen estos Codigos de Creditos: \n"
                        + msgTipoCredito));
            }
            if (!isCustomerType || Optional.ofNullable(m.getCustomerType()).isEmpty()) {
                return Mono.just(ResponseEntity.ok(""
                        + "Solo existen estos Codigos de Tipos de Clientes: \n"
                        + msgTipoCliente));
            }

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

                return operations.listByCustomer(m.getCustomer()).filter(p -> p.getCreditType().equals(m.getCreditType())).count().flatMap(fm -> {
                    if (CreditType.CREDIT_CARD.equals(m.getCreditType())) {
                        return operations.create(c).flatMap(rp -> {
                            return Mono.just(ResponseEntity.ok(rp));
                        });
                    } else if (CreditType.BUSINESS_CREDIT.equals(m.getCreditType())) {
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
                if (CreditType.PERSONAL_CREDIT.equals(m.getCreditType())) {
                    return Mono.just(ResponseEntity.ok("Usted no puede tener credito personal!!"));
                } else {
                    return operations.create(c).flatMap(rp -> {
                        return Mono.just(ResponseEntity.ok(rp));
                    });
                }
            }
        });
    }

    @PutMapping("/{id}")
    public Mono<Credit> update(@PathVariable("id") String id, @RequestBody Credit c) {
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
        int number = rnd.nextInt(9999);
        return String.format("%04d", number);
    }
}
