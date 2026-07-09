package com.transit.maintenances;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MaintenancesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaintenancesApplication.class, args);
	}

}
