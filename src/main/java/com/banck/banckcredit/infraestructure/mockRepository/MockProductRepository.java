package com.banck.banckcredit.infraestructure.mockRepository;

import com.banck.banckcredit.domain.Product;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.banck.banckcredit.aplication.model.ProductRepository;

/**
 *
 * @author jonavcar
 */
@Component
public class MockProductRepository implements ProductRepository {

    @Override
    public Mono<Product> get(String credito) {
        Product c = new Product();
        c.setProduct("34984545");
        c.setCustomer("CTP");
        return Mono.just(c);
    }

    @Override
    public Flux<Product> list() {
        List<Product> lc = new ArrayList<>();
        Product c = new Product();
        c.setProduct("34984545");
        c.setCustomer("CTP");
        lc.add(c);
        return Flux.fromIterable(lc);
    }

    @Override
    public Mono<Product> create(Product c) {
        return Mono.just(c);
    }

    @Override
    public Mono<Product> update(String credito, Product c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String dniRuc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Flux<Product> listByCustomer(String customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Flux<Product> listByCustomerAndProductType(String customer, String productType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
