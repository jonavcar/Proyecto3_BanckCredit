package com.banck.banckcredit.infraestructure.repository;

import com.banck.banckcredit.infraestructure.model.dao.ProductDao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 *
 * @author jonavcar
 */
public interface IProductCrudRepository extends ReactiveCrudRepository<ProductDao, String> {

    Flux<ProductDao> findAllByCustomer(String customer);

    Flux<ProductDao> findAllByCustomerAndCreditType(String customer, String creditType);
}
