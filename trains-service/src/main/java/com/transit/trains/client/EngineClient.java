package com.transit.trains.client;

import com.transit.trains.dto.response.EngineResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// If you have service discovery (Eureka), use:
// @FeignClient(name = "engine-service")
// Otherwise, hardcode the URL for now:
@FeignClient(name = "engine-service")
public interface EngineClient {

    @GetMapping("/api/engines/{id}")
    EngineResponseDTO getEngineById(@PathVariable("id") Long id);
}