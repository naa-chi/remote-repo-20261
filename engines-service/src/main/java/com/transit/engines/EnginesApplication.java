package com.transit.engines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EnginesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnginesApplication.class, args);
	}

}
