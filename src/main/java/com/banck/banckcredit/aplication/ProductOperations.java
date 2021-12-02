package com.banck.banckcredit.aplication;

import com.banck.banckcredit.domain.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jonavcar
 */
public interface ProductOperations {

    public Flux<Product> list();
    
    public Flux<Product> listByCustomer(String customer);

    public Mono<Product> get(String id);
    
    public Mono<Product> create(Product credit);

    public Mono<Product> update(String id, Product credit);

    public void delete(String id);
    
    public Mono<Integer> countCardByCustomer(String customer);

}
