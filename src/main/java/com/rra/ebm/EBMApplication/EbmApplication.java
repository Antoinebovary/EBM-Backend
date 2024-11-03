package com.rra.ebm.EBMApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.rra.ebm.EBMApplication.domain"})
@EnableJpaRepositories(basePackages = {"com.rra.ebm.EBMApplication.repository"})
public class EbmApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbmApplication.class, args);
    }
}

