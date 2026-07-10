package com.transit.managers.fallback;

import com.transit.managers.dto.response.ManagerResponseDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class ManagerServiceFallback {

    public ManagerResponseDTO getManagerFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for Manager ID " + id + ". Error: " + t.getMessage());
        return createFallbackManager();
    }

    public ManagerResponseDTO getManagerFallback(String code, Throwable t) {
        System.err.println("CircuitBreaker triggered for Manager code " + code + ". Error: " + t.getMessage());
        return createFallbackManager();
    }

    public List<ManagerResponseDTO> getManagersFallbackList(Throwable t) {
        System.err.println("CircuitBreaker triggered for managers list. Error: " + t.getMessage());
        return Collections.singletonList(createFallbackManager());
    }

    private ManagerResponseDTO createFallbackManager() {
        ManagerResponseDTO fallback = new ManagerResponseDTO();
        fallback.setId(-1L);
        fallback.setCode("ERROR");
        fallback.setSalary(0L);
        fallback.setContractDate(Date.valueOf(LocalDate.now()));
        fallback.setFirstName("ERROR");
        fallback.setSecondName("ERROR");
        fallback.setManagerGroup("ERR");
        return fallback;
    }
}