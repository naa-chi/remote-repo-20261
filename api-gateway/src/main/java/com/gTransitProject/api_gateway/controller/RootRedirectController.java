package com.gTransitProject.api_gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.URI;

@Controller
public class RootRedirectController {

    @GetMapping("/")
    public Mono<Void> redirectRoot(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FOUND);
        exchange.getResponse().getHeaders().setLocation(URI.create("/swagger-ui.html"));
        return exchange.getResponse().setComplete();
    }
}