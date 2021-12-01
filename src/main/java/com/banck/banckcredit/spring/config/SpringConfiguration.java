package com.banck.banckcredit.spring.config;

import com.banck.banckcredit.infraestructure.repository.CreditCrudRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.banck.banckcredit.aplication.model.CreditRepository;

/**
 *
 * @author jonavcar
 */
@Configuration
public class SpringConfiguration {
   
    @Bean
    public CreditRepository creditRepository() {
        return new CreditCrudRepository();
    }
}
