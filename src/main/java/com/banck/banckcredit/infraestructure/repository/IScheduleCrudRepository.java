/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banck.banckcredit.infraestructure.repository;

import com.banck.banckcredit.infraestructure.model.dao.ScheduleDao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 *
 * @author jnacarra
 */
public interface IScheduleCrudRepository extends ReactiveCrudRepository<ScheduleDao, String> {

    Flux<ScheduleDao> findAllByCustomer(String customer);

    Flux<ScheduleDao> findAllByProduct(String customer);

    Flux<ScheduleDao> findAllByCustomerAndProduct(String customer, String product);
    
    Flux<ScheduleDao> findAllByCustomerAndStatus(String customer, String status);
}
