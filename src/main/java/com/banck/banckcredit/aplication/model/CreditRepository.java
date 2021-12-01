package com.banck.banckcredit.aplication.model;

import com.banck.banckcredit.domain.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jonavcar
 */
public interface CreditRepository {

    public Flux<Credit> list();

    public Flux<Credit> listByCustomer(String customer);

    public Flux<Credit> listByCustomerAndCreditType(String customer, String creditType);

    public Mono<Credit> get(String id);

    public Mono<Credit> create(Credit credit);

    public Mono<Credit> update(String id, Credit credit);

    public void delete(String id);
}
