package com.banck.banckcredit.infraestructure.repository;

import com.banck.banckcredit.infraestructure.model.dao.CreditDao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 *
 * @author jonavcar
 */
public interface ICreditCrudRepository extends ReactiveCrudRepository<CreditDao, String> {

    Flux<CreditDao> findAllByCustomer(String customer);

    Flux<CreditDao> findAllByCustomerAndCreditType(String customer, String creditType);
}
