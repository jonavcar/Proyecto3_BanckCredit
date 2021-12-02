/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banck.banckcredit.aplication.impl;

import com.banck.banckcredit.aplication.ScheduleOperations;
import com.banck.banckcredit.aplication.model.ScheduleRepository;
import com.banck.banckcredit.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jnacarra
 */
@Service
@RequiredArgsConstructor
public class ScheduleOperationsImpl implements ScheduleOperations {

    Logger logger = LoggerFactory.getLogger(ProductOperationsImpl.class);
    private final ScheduleRepository scheduleRepository;

    @Override
    public Flux<Schedule> list() {
        return scheduleRepository.list();
    }

    @Override
    public Flux<Schedule> listByCustomer(String customer) {
        return scheduleRepository.listByCustomer(customer);
    }

    @Override
    public Mono<Schedule> get(String id) {
        return scheduleRepository.get(id);
    }

    @Override
    public Mono<Schedule> create(Schedule schedule) {
        return scheduleRepository.create(schedule);
    }

    @Override
    public Mono<Schedule> update(String id, Schedule schedule) {
        return scheduleRepository.update(id, schedule);
    }

    @Override
    public void delete(String id) {
        scheduleRepository.delete(id);
    }

}
