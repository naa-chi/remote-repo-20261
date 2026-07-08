package com.transit.reviews.client;

import com.transit.reviews.dto.response.TicketResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tickets-service", url = "http://localhost:7772")
public interface TicketClient {

    @GetMapping("/api/tickets/code/{code}")
    TicketResponseDTO getTicketByCode(@PathVariable("code") String code);
}