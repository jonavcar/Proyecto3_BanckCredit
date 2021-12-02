package com.banck.banckcredit.aplication.impl;

import com.banck.banckcredit.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.banck.banckcredit.utils.ProductType;
import com.banck.banckcredit.aplication.model.ProductRepository;
import com.banck.banckcredit.aplication.ProductOperations;

/**
 *
 * @author jonavcar
 */
@Service
@RequiredArgsConstructor
public class ProductOperationsImpl implements ProductOperations {

    Logger logger = LoggerFactory.getLogger(ProductOperationsImpl.class);
    private final ProductRepository creditRepository;

    @Override
    public Flux<Product> list() {
        return creditRepository.list();
    }

    @Override
    public Mono<Product> get(String credito) {
        return creditRepository.get(credito);
    }

    @Override
    public Mono<Product> create(Product credit) {
        return creditRepository.create(credit);
    }

    @Override
    public Mono<Product> update(String credito, Product c
    ) {
        return creditRepository.update(credito, c);
    }

    @Override
    public void delete(String credito) {
        creditRepository.delete(credito);
    }

    @Override
    public Flux<Product> listByCustomer(String customer) {
        return creditRepository.listByCustomer(customer);
    }

    @Override
    public Mono<Integer> countCardByCustomer(String customer) {
        return creditRepository.listByCustomerAndCreditType(customer, ProductType.CREDIT_CARD.value).count().flatMap(r -> {
            return Mono.just(r.intValue());
        });
    }

}
