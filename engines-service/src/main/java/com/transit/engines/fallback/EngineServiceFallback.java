package com.transit.engines.fallback;

import com.transit.engines.dto.EngineDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class EngineServiceFallback {

    public EngineDTO getEngineFallback(Long id, Throwable t) {
        // Fixed "Train ID" to "Engine ID"
        System.err.println("Circuitbreaker triggered for Engine ID " + id + ". Error: " + t.getMessage());
        return createDefaultFallbackEngine(id != null ? id : -1L);
    }

    public List<EngineDTO> getEnginesByManufacturerIdFallback(String manufacturerId, Throwable t) {
        System.err.println("Circuitbreaker triggered for Manufacturer ID " + manufacturerId + ". Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackEngine(-1L));
    }

    public List<EngineDTO> getEnginesByEngineCodeFallback(String engineCode, Throwable t) {
        System.err.println("Circuitbreaker triggered for Engine Code " + engineCode + ". Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackEngine(-1L));
    }

    public List<EngineDTO> getEngineHorsepowerFallback(float engineHorsepower, Throwable t) {
        System.err.println("Circuitbreaker triggered for Horsepower " + engineHorsepower + ". Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackEngine(-1L));
    }

    public List<EngineDTO> getEnginePriceFallback(float enginePrice, Throwable t) {
        System.err.println("Circuitbreaker triggered for Price " + enginePrice + ". Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackEngine(-1L));
    }

    public List<EngineDTO> getEngineWeightFallback(float engineWeight, Throwable t) {
        System.err.println("Circuitbreaker triggered for Weight " + engineWeight + ". Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackEngine(-1L));
    }

    public List<EngineDTO> getAllEnginesFallback(Throwable t) {
        System.err.println("Circuitbreaker triggered for get all engines. Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackEngine(-1L));
    }

    // ==========================================
    // HELPER METHOD
    // ==========================================

    private EngineDTO createDefaultFallbackEngine(Long id) {
        EngineDTO fallbackDto = new EngineDTO();
        fallbackDto.setId(id);
        fallbackDto.setEngineCode("ERROR");
        fallbackDto.setManufacturerId("Unavailable");
        fallbackDto.setEngineHorsepower(0f);
        fallbackDto.setEnginePrice(0f);
        fallbackDto.setEngineWeight(0f);
        fallbackDto.setProductionDate(Date.valueOf(LocalDate.now()));

        return fallbackDto;
    }
}