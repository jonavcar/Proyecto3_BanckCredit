package com.banck.banckcredit.infraestructure.rest;

import com.banck.banckcredit.aplication.ProductOperations;
import com.banck.banckcredit.domain.Schedule;
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
import com.banck.banckcredit.aplication.impl.ScheduleOperationsImpl;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import com.banck.banckcredit.aplication.ScheduleOperations;
import com.banck.banckcredit.utils.Concept;
import com.banck.banckcredit.utils.DateValidator;
import com.banck.banckcredit.utils.DateValidatorUsingLocalDate;
import com.banck.banckcredit.utils.Status;
import org.springframework.http.HttpStatus;

/**
 *
 * @author jonavcar
 */
@RestController
@RequestMapping("/credit/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    Logger logger = LoggerFactory.getLogger(ScheduleOperationsImpl.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/Bogota"));
    private final ScheduleOperations operations;
    private final ProductOperations productOperations;

    @GetMapping
    public Flux<Schedule> listAll() {
        return operations.list();
    }

    @GetMapping("/{id}")
    public Mono<Schedule> get(@PathVariable("id") String id) {
        return operations.get(id);
    }

    @GetMapping("/{customer}/list")
    public Flux<Schedule> listAccountByCustomer(@PathVariable("customer") String customer) {
        return operations.listByCustomer(customer);
    }

    @PostMapping
    public Mono<ResponseEntity> create(@RequestBody Schedule rqSchedule) {
        rqSchedule.setDate(dateTime.format(formatDate));
        rqSchedule.setTime(dateTime.format(formatTime));

        return Mono.just(rqSchedule).flatMap(schedule -> {
            /*String msgTipoProducto
                    = "Credito Personal = { \"productType\": \"CP\" }\n"
                    + "Credito Empresarial = { \"productType\": \"CE\" }\n"
                    + "Targeta Debito = { \"productType\": \"TD\" }\n"
                    + "Targeta Credito = { \"productType\": \"TC\" }";

            if (Optional.ofNullable(schedule.getProductType()).isEmpty()) {
                return Mono.just(new ResponseEntity(""
                        + "Debe ingresar Tipo Producto, Ejemplos: \n"
                        + msgTipoProducto, HttpStatus.BAD_REQUEST));
            }*/

            if (Optional.ofNullable(schedule.getExpirationDate()).isEmpty()) {
                return Mono.just(new ResponseEntity("Debe ingresar Fecha Vencimiento, Ejemplo { \"expirationDate\": \"11-02-2021\" }", HttpStatus.BAD_REQUEST));
            }

            if (Optional.ofNullable(schedule.getProduct()).isEmpty()) {
                return Mono.just(new ResponseEntity("Debe ingresar el Producto, Ejemplo { \"product\": \"TC-985433-12\" }", HttpStatus.BAD_REQUEST));
            }

            if (Optional.ofNullable(schedule.getAmount()).isEmpty() || schedule.getAmount() <= 0) {
                return Mono.just(new ResponseEntity("Debe ingresar la Cuota, Ejemplo { \"amount\": 240 }", HttpStatus.BAD_REQUEST));
            }

            DateValidator validator = new DateValidatorUsingLocalDate(formatDate);

            if (!validator.isValid(schedule.getExpirationDate())) {
                return Mono.just(new ResponseEntity("Debe ingresar la fecha correctamente con el formato dd-mm-yyyy", HttpStatus.BAD_REQUEST));
            }

            /*boolean isProductType = false;
            for (ProductType tc : ProductType.values()) {
                if (schedule.getProductType().equals(tc.value)) {
                    isProductType = true;
                }
            }

            if (!isProductType || Optional.ofNullable(schedule.getProductType()).isEmpty()) {
                return Mono.just(new ResponseEntity(""
                        + "Solo existen estos Codigos de Productos: \n"
                        + msgTipoProducto, HttpStatus.BAD_REQUEST));
            }*/
            schedule.setSchedule(schedule.getProduct() + "" + getRandomNumberString());
            schedule.setConcept(Concept.DEADLINE_FEE.value);
            schedule.setInterest(0.3);
            schedule.setTotal(schedule.getAmount() + (schedule.getAmount() * schedule.getInterest()));
            schedule.setObservations("Compra Megaplaza al credito en Cuotas");
            schedule.setStatus(Status.PENDING.value);
            return productOperations.get(schedule.getProduct()).flatMap(gProd -> {
                schedule.setCustomer(gProd.getCustomer());
                schedule.setProductType(gProd.getProductType());
                return operations.create(schedule).flatMap(scheduleCreated -> {
                    return Mono.just(new ResponseEntity(scheduleCreated, HttpStatus.OK));
                });
            }).switchIfEmpty(Mono.just(new ResponseEntity("No se encontro el producto " + schedule.getProduct(), HttpStatus.BAD_REQUEST)));

        });
    }

    @PutMapping("/quota/{id}")
    public Mono<ResponseEntity> changeStatusCuot(@PathVariable("id") String id, @RequestBody Schedule rqSchedule) {

        String msgStatus
                = "Cuota Pendiente = { \"status\": \"PENDIENTE\" }\n"
                + "Cuota Pagada = { \"status\": \"PAGADO\" }\n"
                + "Cuota Vencida = { \"status\": \"VENCIDO\" }";

        if (Optional.ofNullable(rqSchedule.getStatus()).isEmpty()) {
            return Mono.just(new ResponseEntity(""
                    + "Debe ingresar el Estado, Ejemplos: \n"
                    + msgStatus, HttpStatus.BAD_REQUEST));
        }

        boolean isStatus = false;
        for (Status tc : Status.values()) {
            if (rqSchedule.getStatus().toUpperCase().equals(tc.value)) {
                isStatus = true;
            }
        }

        if (!isStatus || Optional.ofNullable(rqSchedule.getStatus()).isEmpty()) {
            return Mono.just(new ResponseEntity(""
                    + "Solo existen estos Codigos de Estado: \n"
                    + msgStatus, HttpStatus.BAD_REQUEST));
        }
        return operations.get(id).flatMap(schedule -> {
            schedule.setStatus(rqSchedule.getStatus());
            return operations.update(schedule.getSchedule(), schedule).flatMap(tt -> {
                return Mono.just(new ResponseEntity("La cuota se cambio a estado " + schedule.getStatus(), HttpStatus.OK));
            });

        }).switchIfEmpty(Mono.just(new ResponseEntity("La cuota  " + id + " No existe!!", HttpStatus.BAD_REQUEST)));
    }

    @PutMapping("/{id}")
    public Mono<Schedule> update(@PathVariable("id") String id, @RequestBody Schedule c) {
        return operations.update(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        operations.delete(id);
    }

    @GetMapping("/card/{customer}/count")
    public Mono<Integer> countCardByCustomer(@PathVariable("customer") String customer) {
        return null;
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999);
        return String.format("%03d", number);
    }

    public Mono<Boolean> existProduct(String product) {
        return productOperations.get(product).flatMap(gProd -> {
            return Mono.just(true);
        }).switchIfEmpty(Mono.just(false));
    }
}
