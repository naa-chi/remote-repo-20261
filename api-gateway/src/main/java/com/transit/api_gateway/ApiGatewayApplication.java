package com.transit.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Ticket Service
            .route("ticket-route", r -> r
                .path("/tickets-service/**", "/tickets-service")
                .filters(f -> f.rewritePath("/tickets-service(?<segment>.*)", "/api/tickets${segment}"))
                .uri("lb://tickets-service")
            )
            // Review Service
            .route("review-route", r -> r
                .path("/reviews-service/**", "/reviews-service")
                .filters(f -> f.rewritePath("/reviews-service(?<segment>.*)", "/api/reviews${segment}"))
                .uri("lb://reviews-service")
            )
            // Maintenance Service
            .route("maintenance-route", r -> r
                .path("/maintenances-service/**", "/maintenances-service")
                .filters(f -> f.rewritePath("/maintenances-service(?<segment>.*)", "/api/maintenances${segment}"))
                .uri("lb://maintenances-service")
            )
            // Engine Service
            .route("engine-route", r -> r
                .path("/engines-service/**", "/engines-service")
                .filters(f -> f.rewritePath("/engines-service(?<segment>.*)", "/api/engines${segment}"))
                .uri("lb://engines-service")
            )
            // Train Service
            .route("train-route", r -> r
                .path("/trains-service/**", "/trains-service")
                .filters(f -> f.rewritePath("/trains-service(?<segment>.*)", "/api/trains${segment}"))
                .uri("lb://trains-service")
            )
            // Manufacturer Service
            .route("manufacturer-route", r -> r
                .path("/manufacturers-service/**", "/manufacturers-service")
                .filters(f -> f.rewritePath("/manufacturers-service(?<segment>.*)", "/api/manufacturers${segment}"))
                .uri("lb://manufacturers-service")
            )
            .route("station-route", r -> r
                .path("/stations-service/**", "/stations-service")
                .filters(f -> f.rewritePath("/stations-service(?<segment>.*)", "/api/stations${segment}"))
                .uri("lb://stations-service")
            )
            .route("line-route", r -> r
                .path("/lines-service/**", "/lines-service")
                .filters(f -> f.rewritePath("/lines-service(?<segment>.*)", "/api/lines${segment}"))
                .uri("lb://lines-service")
            )
            .route("city-route", r -> r
                .path("/cities-service/**", "/cities-service")
                .filters(f -> f.rewritePath("/cities-service(?<segment>.*)", "/api/cities${segment}"))
                .uri("lb://cities-service")
            )
            .route("manager-route", r -> r
                .path("/managers-service/**", "/managers-service")
                .filters(f -> f.rewritePath("/managers-service(?<segment>.*)", "/api/managers${segment}"))
                .uri("lb://managers-service")
            )
            .route("driver-route", r -> r
                .path("/drivers-service/**", "/drivers-service")
                .filters(f -> f.rewritePath("/drivers-service(?<segment>.*)", "/api/drivers${segment}"))
                .uri("lb://drivers-service")
            )
            .route("client-route", r -> r
                .path("/clients-service/**", "/clients-service")
                .filters(f -> f.rewritePath("/clients-service(?<segment>.*)", "/api/clients${segment}"))
                .uri("lb://clients-service")
            )
            .build();
    }
}