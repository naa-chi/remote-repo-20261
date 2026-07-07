package com.transit.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("engine-route", r -> r
                .path("/engines-service/**")
                .filters(f -> f.rewritePath("/engines-service/(?<segment>.*)", "/api/engines/${segment}"))
                .uri("http://localhost:7771")
            )
            .route("train-route", r -> r
                .path("/trains-service/**")
                .filters(f -> f.rewritePath("/trains-service/(?<segment>.*)", "/api/trains/${segment}"))
                .uri("http://localhost:7770")
            )
            .build();
    }
}