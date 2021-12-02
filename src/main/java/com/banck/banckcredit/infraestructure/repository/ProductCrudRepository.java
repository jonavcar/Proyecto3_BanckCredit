package com.banck.banckcredit.infraestructure.repository;

import com.banck.banckcredit.domain.Product;
import com.banck.banckcredit.infraestructure.model.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.banck.banckcredit.aplication.model.ProductRepository;

/**
 *
 * @author jonavcar
 */
@Component
public class ProductCrudRepository implements ProductRepository {

    @Autowired
    IProductCrudRepository productRepository;

    @Override
    public Mono<Product> get(String id) {
        return productRepository.findById(id).map(this::ProductDaoToProduct);
    }

    @Override
    public Flux<Product> list() {
        return productRepository.findAll().map(this::ProductDaoToProduct);
    }

    @Override
    public Mono<Product> create(Product c) {
        return productRepository.save(ProductToProductDao(c)).map(this::ProductDaoToProduct);
    }

    @Override
    public Mono<Product> update(String id, Product credit) {
        credit.setProduct(id);
        return productRepository.save(ProductToProductDao(credit)).map(this::ProductDaoToProduct);
    }

    @Override
    public void delete(String id) {
        productRepository.deleteById(id).subscribe();
    }

    @Override
    public Flux<Product> listByCustomer(String customer) {
        return productRepository.findAllByCustomer(customer).map(this::ProductDaoToProduct);
    }

    public ProductDao ProductToProductDao(Product p) {
        ProductDao pd = new ProductDao();
        pd.setProduct(p.getProduct());
        pd.setProductType(p.getProductType());
        pd.setCustomer(p.getCustomer());
        pd.setCustomerType(p.getCustomerType());
        pd.setDate(p.getDate());
        pd.setStatus(p.isStatus());
        return pd;
    }

    public Product ProductDaoToProduct(ProductDao pd) {
        Product p = new Product();
        p.setProduct(pd.getProduct());
        p.setProductType(pd.getProductType());
        p.setCustomer(pd.getCustomer());
        p.setCustomerType(pd.getCustomerType());
        p.setDate(pd.getDate());
        p.setStatus(pd.isStatus());
        return p;
    }

    @Override
    public Flux<Product> listByCustomerAndCreditType(String customer, String creditType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 

}
