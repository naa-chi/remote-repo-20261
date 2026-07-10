package com.transit.drivers.fallback;

import com.transit.drivers.dto.response.DriverResponseDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class DriverServiceFallback {

    public DriverResponseDTO getDriverFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for Driver ID " + id + ". Error: " + t.getMessage());
        return createFallbackDriver();
    }

    public DriverResponseDTO getDriverFallback(String code, Throwable t) {
        System.err.println("CircuitBreaker triggered for Driver code " + code + ". Error: " + t.getMessage());
        return createFallbackDriver();
    }

    public List<DriverResponseDTO> getDriversFallbackList(Throwable t) {
        System.err.println("CircuitBreaker triggered for drivers list. Error: " + t.getMessage());
        return Collections.singletonList(createFallbackDriver());
    }

    private DriverResponseDTO createFallbackDriver() {
        DriverResponseDTO fallback = new DriverResponseDTO();
        fallback.setId(-1L);
        fallback.setCode("ERROR");
        fallback.setSalary(0L);
        fallback.setContractDate(Date.valueOf(LocalDate.now()));
        fallback.setDateOfBirth(Date.valueOf(LocalDate.now()));
        fallback.setFirstName("ERROR");
        fallback.setSecondName("ERROR");
        fallback.setCapacitatedCode("ERR");
        return fallback;
    }
}