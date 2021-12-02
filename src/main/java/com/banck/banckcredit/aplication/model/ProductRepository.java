package com.banck.banckcredit.aplication.model;

import com.banck.banckcredit.domain.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jonavcar
 */
public interface ProductRepository {

    public Flux<Product> list();

    public Flux<Product> listByCustomer(String customer);

    public Flux<Product> listByCustomerAndProductType(String customer, String productType);

    public Mono<Product> get(String id);

    public Mono<Product> create(Product product);

    public Mono<Product> update(String id, Product product);

    public void delete(String id);
}
