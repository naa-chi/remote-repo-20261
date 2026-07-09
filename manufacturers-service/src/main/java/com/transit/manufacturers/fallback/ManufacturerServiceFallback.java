package com.transit.manufacturers.fallback;

import com.transit.manufacturers.dto.response.ManufacturerResponseDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class ManufacturerServiceFallback {

    public ManufacturerResponseDTO getManufacturerFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for Manufacturer ID " + id + ". Error: " + t.getMessage());
        return createDefaultFallback(-1L);
    }

    public ManufacturerResponseDTO getManufacturerFallback(String manufacturerId, Throwable t) {
        System.err.println("CircuitBreaker triggered for Manufacturer code " + manufacturerId + ". Error: " + t.getMessage());
        return createDefaultFallback(-1L);
    }

    public List<ManufacturerResponseDTO> getManufacturersFallbackList(Throwable t) {
        System.err.println("CircuitBreaker triggered for manufacturers list. Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallback(-1L));
    }

    private ManufacturerResponseDTO createDefaultFallback(Long id) {
        ManufacturerResponseDTO fallback = new ManufacturerResponseDTO();
        fallback.setId(id);
        fallback.setManufacturerId("ERROR");
        fallback.setCountryOfOrigin("ERR");
        fallback.setFoundingDate(Date.valueOf(LocalDate.now()));
        fallback.setRevenue(0L);
        return fallback;
    }
}