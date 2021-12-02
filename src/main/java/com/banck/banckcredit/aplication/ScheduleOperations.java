/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banck.banckcredit.aplication;

import com.banck.banckcredit.domain.Schedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jnacarra
 */
public interface ScheduleOperations {

    public Flux<Schedule> list();

    public Flux<Schedule> listByCustomer(String customer);

    public Mono<Schedule> get(String id);

    public Mono<Schedule> create(Schedule schedule);

    public Mono<Schedule> update(String id, Schedule schedule);

    public void delete(String id);
}
