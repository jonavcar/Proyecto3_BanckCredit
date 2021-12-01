package com.banck.banckcredit.aplication;

import com.banck.banckcredit.domain.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jonavcar
 */
public interface CreditOperations {

    public Flux<Credit> list();
    
    public Flux<Credit> listByCustomer(String customer);

    public Mono<Credit> get(String id);
    
    public Mono<Credit> create(Credit credit);

    public Mono<Credit> update(String id, Credit credit);

    public void delete(String id);
    
    public Mono<Integer> countCardByCustomer(String customer);

}
