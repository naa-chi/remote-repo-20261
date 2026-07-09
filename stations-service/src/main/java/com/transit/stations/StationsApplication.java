package com.transit.stations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients   // <-- Add this
public class StationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StationsApplication.class, args);
    }
}