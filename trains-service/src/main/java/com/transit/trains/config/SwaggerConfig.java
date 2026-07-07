package com.transit.trains.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API2026 Maintenance Service")
                        .version("1.0")
                        .description("API Docs for the Trains Service of the transit project"));

                        //full rewrite time!
    }
}

