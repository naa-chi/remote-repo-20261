package com.transit.clients.fallback;

import com.transit.clients.dto.response.ClientResponseDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class ClientServiceFallback {

    public ClientResponseDTO getClientFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for Client ID " + id + ". Error: " + t.getMessage());
        return createFallbackClient();
    }

    public ClientResponseDTO getClientFallback(String code, Throwable t) {
        System.err.println("CircuitBreaker triggered for Client code " + code + ". Error: " + t.getMessage());
        return createFallbackClient();
    }

    public List<ClientResponseDTO> getClientsFallbackList(Throwable t) {
        System.err.println("CircuitBreaker triggered for clients list. Error: " + t.getMessage());
        return Collections.singletonList(createFallbackClient());
    }

    private ClientResponseDTO createFallbackClient() {
        ClientResponseDTO fallback = new ClientResponseDTO();
        fallback.setId(-1L);
        fallback.setCode("ERROR");
        fallback.setFirstName("ERROR");
        fallback.setLastName("ERROR");
        fallback.setEmail("error@transit.com");
        fallback.setPhoneNumber("0000000000");
        fallback.setRegistrationDate(Date.valueOf(LocalDate.now()));
        return fallback;
    }
}