package com.banck.banckcredit.aplication.impl;

import com.banck.banckcredit.domain.Credit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.banck.banckcredit.aplication.CreditOperations;
import com.banck.banckcredit.aplication.model.CreditRepository;
import com.banck.banckcredit.utils.CreditType;

/**
 *
 * @author jonavcar
 */
@Service
@RequiredArgsConstructor
public class CreditOperationsImpl implements CreditOperations {

    Logger logger = LoggerFactory.getLogger(CreditOperationsImpl.class);
    private final CreditRepository creditRepository;

    @Override
    public Flux<Credit> list() {
        return creditRepository.list();
    }

    @Override
    public Mono<Credit> get(String credito) {
        return creditRepository.get(credito);
    }

    @Override
    public Mono<Credit> create(Credit credit) {
        return creditRepository.create(credit);
    }

    @Override
    public Mono<Credit> update(String credito, Credit c
    ) {
        return creditRepository.update(credito, c);
    }

    @Override
    public void delete(String credito) {
        creditRepository.delete(credito);
    }

    @Override
    public Flux<Credit> listByCustomer(String customer) {
        return creditRepository.listByCustomer(customer);
    }

    @Override
    public Mono<Integer> countCardByCustomer(String customer) {
        return creditRepository.listByCustomerAndCreditType(customer, CreditType.CREDIT_CARD.value).count().flatMap(r -> {
            return Mono.just(r.intValue());
        });
    }

}
