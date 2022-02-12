package com.ncedu.fooddelivery;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories("com.ncedu.fooddelivery.api.v1.repos")
public class FoodDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodDeliveryApplication.class, args);
    }

    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }

}