package com.transit.trains;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.transit.trains.repository")
public class TrainsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainsApplication.class, args);
	}

}
