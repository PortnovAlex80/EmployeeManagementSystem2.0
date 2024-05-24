package com.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ems.repository")
@EntityScan(basePackages = "com.ems.model")
public class EmsApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EmsApplication.class, args);
    }
}
