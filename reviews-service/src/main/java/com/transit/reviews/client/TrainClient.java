package com.transit.reviews.client;

import com.transit.reviews.dto.response.TrainResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "trains-service", url = "http://localhost:7770")
public interface TrainClient {

    @GetMapping("/api/trains/{id}")
    TrainResponseDTO getTrainById(@PathVariable("id") Long id);
}