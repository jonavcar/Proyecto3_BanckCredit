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
import com.banck.banckcredit.domain.ProductSummaryDto;

/**
 *
 * @author jonavcar
 */
@Service
@RequiredArgsConstructor
public class ProductOperationsImpl implements ProductOperations {

    Logger logger = LoggerFactory.getLogger(ProductOperationsImpl.class);
    private final ProductRepository productRepository;

    @Override
    public Flux<Product> list() {
        return productRepository.list();
    }

    @Override
    public Mono<Product> get(String credito) {
        return productRepository.get(credito);
    }

    @Override
    public Mono<Product> create(Product credit) {
        return productRepository.create(credit);
    }

    @Override
    public Mono<Product> update(String credito, Product c
    ) {
        return productRepository.update(credito, c);
    }

    @Override
    public void delete(String credito) {
        productRepository.delete(credito);
    }

    @Override
    public Flux<Product> listByCustomer(String customer) {
        return productRepository.listByCustomer(customer);
    }

    @Override
    public Mono<Integer> countCardByCustomer(String customer) {
        return productRepository.listByCustomerAndProductType(customer, ProductType.CREDIT_CARD.value).count().flatMap(r -> {
            return Mono.just(r.intValue());
        });
    }

    @Override
    public Flux<ProductSummaryDto> listProductSummaryByCustomer(String customer) {
        return productRepository.listByCustomer(customer)
                .map(f -> {
                    ProductSummaryDto ps = new ProductSummaryDto();
                    ps.setCustomer(f.getCustomer());
                    ps.setProduct(f.getProduct());
                    ps.setDescription(f.getProductType());
                    return ps;
                });
    }

}
