package com.transit.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity //eventually we'll add proper jwt but to hell with it rn
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable()) // Disables Cross-Site Request Forgery protection (needed for POST requests via Postman)
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll() // Allows everything through without logging in
            );
        return http.build();
    }
}