package com.transit.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("train-service-route", r -> r
                .path("/trains-service/**")
                .filters(f -> f.rewritePath("/trains-service/(?<segment>.*)", "/api/trains/${segment}"))
                .uri("http://localhost:7770"))
            .route("engine-service-route", r -> r
                .path("/engines-service/**")
                .filters(f -> f.rewritePath("/engines-service/(?<segment>.*)", "/api/engines/${segment}"))
                .uri("http://localhost:7771"))
            .build();
    }
}