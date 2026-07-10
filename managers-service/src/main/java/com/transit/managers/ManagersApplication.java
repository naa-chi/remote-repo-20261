package com.transit.managers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients   // Not strictly needed if no Feign calls, but keep for consistency
public class ManagersApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagersApplication.class, args);
    }
}