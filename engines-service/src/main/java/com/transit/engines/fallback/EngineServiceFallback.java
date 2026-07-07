package com.transit.engines.fallback;

import com.transit.engines.dto.response.EngineResponseDTO;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class EngineServiceFallback {

    public EngineResponseDTO getEngineFallback(Long id, Throwable t) {
        System.err.println("Circuitbreaker triggered for Engine ID " + id + ". Error: " + t.getMessage());
        return createDefaultFallbackEngine(id != null ? id : -1L);
    }

    public List<EngineResponseDTO> getEnginesFallbackList(Throwable t) {
        System.err.println("Circuitbreaker triggered for engines list view. Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackEngine(-1L));
    }

    private EngineResponseDTO createDefaultFallbackEngine(Long id) {
        EngineResponseDTO fallbackDto = new EngineResponseDTO();
        fallbackDto.setId(id);
        fallbackDto.setEngineId(0L);
        fallbackDto.setEngineCode("ERROR");
        fallbackDto.setManufacturerId("Unavailable");
        fallbackDto.setEngineHorsepower(0f);
        fallbackDto.setEnginePrice(0f);
        fallbackDto.setEngineWeight(0f);
        fallbackDto.setProductionDate(Date.valueOf(LocalDate.now()));
        return fallbackDto;
    }
}