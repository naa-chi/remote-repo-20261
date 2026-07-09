package com.transit.stations.fallback;

import com.transit.stations.dto.response.StationResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class StationServiceFallback {

    public StationResponseDTO getStationFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for Station ID " + id + ". Error: " + t.getMessage());
        return createFallbackStation();
    }

    public StationResponseDTO getStationFallback(String stationCode, Throwable t) {
        System.err.println("CircuitBreaker triggered for Station code " + stationCode + ". Error: " + t.getMessage());
        return createFallbackStation();
    }

    public List<StationResponseDTO> getStationsFallbackList(Throwable t) {
        System.err.println("CircuitBreaker triggered for stations list. Error: " + t.getMessage());
        return Collections.singletonList(createFallbackStation());
    }

    private StationResponseDTO createFallbackStation() {
        StationResponseDTO fallback = new StationResponseDTO();
        fallback.setId(-1L);
        fallback.setStationCode("ERROR");
        fallback.setCityCode("ERR");
        fallback.setCityName("ERROR");
        fallback.setLineCode1(-1);
        fallback.setLine1Length(0L);
        fallback.setLine1PeopleServed(0L);
        // leave others null or 0
        return fallback;
    }
}