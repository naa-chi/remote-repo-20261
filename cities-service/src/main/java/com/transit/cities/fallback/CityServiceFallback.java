package com.transit.cities.fallback;

import com.transit.cities.dto.response.CityResponseDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class CityServiceFallback {
    public CityResponseDTO getCityFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for City ID " + id);
        return createFallback();
    }
    public CityResponseDTO getCityFallback(String code, Throwable t) {
        System.err.println("CircuitBreaker triggered for City code " + code);
        return createFallback();
    }
    public List<CityResponseDTO> getCitiesFallbackList(Throwable t) {
        System.err.println("CircuitBreaker triggered for cities list");
        return Collections.singletonList(createFallback());
    }
    private CityResponseDTO createFallback() {
        CityResponseDTO fallback = new CityResponseDTO();
        fallback.setId(-1L);
        fallback.setThreeLetterCityCode("ERR");
        fallback.setFullCityName("ERROR");
        fallback.setFoundingCityDate(Date.valueOf(LocalDate.now()));
        fallback.setCityPopulation(0L);
        fallback.setCountryCode("ERR");
        return fallback;
    }
}