package com.banck.banckcredit.infraestructure.repository;

import com.banck.banckcredit.aplication.model.ScheduleRepository;
import com.banck.banckcredit.domain.Schedule;
import com.banck.banckcredit.infraestructure.model.dao.ScheduleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jonavcar
 */
@Component
public class ScheduleCrudRepository implements ScheduleRepository {

    @Autowired
    IScheduleCrudRepository scheduleRepository;

    @Override
    public Mono<Schedule> get(String id) {
        return scheduleRepository.findById(id).map(this::ScheduleDaoToSchedule);
    }

    @Override
    public Flux<Schedule> list() {
        return scheduleRepository.findAll().map(this::ScheduleDaoToSchedule);
    }

    @Override
    public Mono<Schedule> create(Schedule c) {
        return scheduleRepository.save(ScheduleToScheduleDao(c)).map(this::ScheduleDaoToSchedule);
    }

    @Override
    public Mono<Schedule> update(String id, Schedule schedule) {
        schedule.setSchedule(id);
        return scheduleRepository.save(ScheduleToScheduleDao(schedule)).map(this::ScheduleDaoToSchedule);
    }

    @Override
    public void delete(String id) {
        scheduleRepository.deleteById(id).subscribe();
    }

    @Override
    public Flux<Schedule> listByCustomer(String customer) {
        return scheduleRepository.findAllByCustomer(customer).map(this::ScheduleDaoToSchedule);
    }

    public ScheduleDao ScheduleToScheduleDao(Schedule s) {
        ScheduleDao sd = new ScheduleDao();
        sd.setSchedule(s.getSchedule());
        sd.setCustomer(s.getCustomer());
        sd.setProduct(s.getProduct());
        sd.setProductType(s.getProductType());
        sd.setConcept(s.getConcept());
        sd.setExpirationDate(s.getExpirationDate());
        sd.setAmount(s.getAmount());
        sd.setInterest(s.getInterest());
        sd.setTotal(s.getTotal());
        sd.setObservations(s.getObservations());
        sd.setDate(s.getDate());
        sd.setTime(s.getTime());
        sd.setStatus(s.getStatus());
        return sd;
    }

    public Schedule ScheduleDaoToSchedule(ScheduleDao sd) {
        Schedule s = new Schedule();
        s.setSchedule(sd.getSchedule());
        s.setCustomer(sd.getCustomer());
        s.setProduct(sd.getProduct());
        s.setProductType(sd.getProductType());
        s.setConcept(sd.getConcept());
        s.setExpirationDate(sd.getExpirationDate());
        s.setAmount(sd.getAmount());
        s.setInterest(sd.getInterest());
        s.setTotal(sd.getTotal());
        s.setObservations(sd.getObservations());
        s.setDate(sd.getDate());
        s.setTime(sd.getTime());
        s.setStatus(sd.getStatus());
        return s;
    }

    @Override
    public Flux<Schedule> listByProduct(String product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Flux<Schedule> listByCustomerAndStatus(String customer, String status) {
       return scheduleRepository.findAllByCustomerAndStatus(customer, status).map(this::ScheduleDaoToSchedule);
    }

}
