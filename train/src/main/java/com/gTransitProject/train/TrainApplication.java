package com.gTransitProject.train;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TrainApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainApplication.class, args);
    }

    // This tells Spring how to create the RestTemplate for your Service
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}