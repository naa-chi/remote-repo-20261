package com.transit.cities.service;

import com.transit.cities.dto.mapper.CityMapper;
import com.transit.cities.dto.request.CityRequestDTO;
import com.transit.cities.dto.response.CityResponseDTO;
import com.transit.cities.fallback.CityServiceFallback;
import com.transit.cities.model.CityModel;
import com.transit.cities.repository.CityRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {
    private final CityRepository repository;
    private final CityMapper mapper;
    private final CityServiceFallback fallback;

    public CityService(CityRepository repository, CityMapper mapper, CityServiceFallback fallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.fallback = fallback;
    }

    @CircuitBreaker(name = "cityService", fallbackMethod = "handleGetCityFallback")
    public CityResponseDTO getCityById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + id));
    }

    @CircuitBreaker(name = "cityService", fallbackMethod = "handleGetCityByCodeFallback")
    public CityResponseDTO getCityByCode(String cityCode) {
        return repository.findByThreeLetterCityCode(cityCode)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("City not found with code: " + cityCode));
    }

    @CircuitBreaker(name = "cityService", fallbackMethod = "handleGetCityFallback")
    public CityResponseDTO createCity(CityRequestDTO request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @CircuitBreaker(name = "cityService", fallbackMethod = "handleGetCityFallback")
    public CityResponseDTO updateCity(Long id, CityRequestDTO request) {
        return repository.findById(id).map(existing -> {
            CityModel updated = mapper.toEntity(request);
            updated.setId(id);
            return mapper.toResponse(repository.save(updated));
        }).orElseThrow(() -> new RuntimeException("City not found with id: " + id));
    }

    @CircuitBreaker(name = "cityService", fallbackMethod = "handleGetCityFallback")
    public void deleteCity(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("City not found with id: " + id);
        repository.deleteById(id);
    }

    @CircuitBreaker(name = "cityService", fallbackMethod = "handleGetCitiesFallbackList")
    public List<CityResponseDTO> getAllCities() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    // Fallback methods
    public CityResponseDTO handleGetCityFallback(Long id, Throwable t) { return fallback.getCityFallback(id, t); }
    public CityResponseDTO handleGetCityByCodeFallback(String code, Throwable t) { return fallback.getCityFallback(code, t); }
    public List<CityResponseDTO> handleGetCitiesFallbackList(Throwable t) { return fallback.getCitiesFallbackList(t); }
}