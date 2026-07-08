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
            // --- Engine Service ---
            .route("engine-route", r -> r
                .path("/engines-service/**")
                .filters(f -> f.rewritePath("/engines-service/(?<segment>.*)", "/api/engines/${segment}"))
                .uri("lb://engines-service")
            )
            // --- Train Service ---
            .route("train-route", r -> r
                .path("/trains-service/**")
                .filters(f -> f.rewritePath("/trains-service/(?<segment>.*)", "/api/trains/${segment}"))
                .uri("lb://trains-service")
            )
            // --- Ticket Service ---
            .route("ticket-route", r -> r
                .path("/tickets-service/**")
                .filters(f -> f.rewritePath("/tickets-service/(?<segment>.*)", "/api/tickets/${segment}"))
                .uri("lb://tickets-service")
            )
            // --- Ticket Service ROOT (no trailing slash) ---
            .route("tickets-root", r -> r
                .path("/tickets-service")
                .filters(f -> f.rewritePath("/tickets-service", "/api/tickets"))
                .uri("lb://tickets-service")
            )
            // --- Review Service ---
            .route("review-route", r -> r
                .path("/reviews-service/**")
                .filters(f -> f.rewritePath("/reviews-service/(?<segment>.*)", "/api/reviews/${segment}"))
                .uri("lb://reviews-service")
            )
            // --- Review Service ROOT (no trailing slash) ---
            .route("reviews-root", r -> r
                .path("/reviews-service")
                .filters(f -> f.rewritePath("/reviews-service", "/api/reviews"))
                .uri("lb://reviews-service")
            )
            .build();
    }
}