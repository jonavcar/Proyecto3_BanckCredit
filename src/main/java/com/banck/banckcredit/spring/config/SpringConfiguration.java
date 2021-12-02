package com.banck.banckcredit.spring.config;

import com.banck.banckcredit.infraestructure.repository.ProductCrudRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.banck.banckcredit.aplication.model.ScheduleRepository;
import com.banck.banckcredit.infraestructure.repository.ScheduleCrudRepository;
import com.banck.banckcredit.aplication.model.ProductRepository;

/**
 *
 * @author jonavcar
 */
@Configuration
public class SpringConfiguration {

    @Bean
    public ProductRepository productRepository() {
        return new ProductCrudRepository();
    }

    @Bean
    public ScheduleRepository scheduleRepository() {
        return new ScheduleCrudRepository();
    }
}
