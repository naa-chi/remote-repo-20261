package com.transit.engines.service;

import com.transit.engines.dto.mapper.EngineAssembler;
import com.transit.engines.dto.request.EngineRequestDTO;
import com.transit.engines.dto.response.EngineResponseDTO;
import com.transit.engines.fallback.EngineServiceFallback;
import com.transit.engines.model.EngineModel;
import com.transit.engines.repository.EnginesRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EngineService {
    private final EnginesRepository repository;
    private final EngineAssembler mapper;
    private final EngineServiceFallback serviceFallback;

    public EngineService(EnginesRepository repository,
                         EngineAssembler mapper,
                         EngineServiceFallback serviceFallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceFallback = serviceFallback;
    }

    // --- Single item ---

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineFallback")
    public EngineResponseDTO getEngineById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Engine not found with id: " + id));
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineFallback")
    public EngineResponseDTO createEngine(EngineRequestDTO engineDTO) {
        EngineModel model = mapper.toEntity(engineDTO);
        return mapper.toResponse(repository.save(model));
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineFallback")
    public EngineResponseDTO updateEngine(Long id, EngineRequestDTO engineDTO) {
        return repository.findById(id).map(existingEngine -> {
            EngineModel updated = mapper.toEntity(engineDTO);
            updated.setId(id);
            return mapper.toResponse(repository.save(updated));
        }).orElseThrow(() -> new RuntimeException("Engine not found with id: " + id));
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineFallback")
    public void deleteEngine(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Engine not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // --- List methods (return empty list instead of throwing) ---

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackString")
    public List<EngineResponseDTO> getEnginesByManufacturerId(String manufacturerId) {
        return repository.findByManufacturerId(manufacturerId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackString")
    public List<EngineResponseDTO> getEnginesByEngineCode(String engineCode) {
        return repository.findByEngineCode(engineCode).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackFloat")
    public List<EngineResponseDTO> getEngineHorsepower(float engineHorsepower) {
        return repository.findByEngineHorsepower(engineHorsepower).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackFloat")
    public List<EngineResponseDTO> getEnginePrice(float enginePrice) {
        return repository.findByEnginePrice(enginePrice).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackFloat")
    public List<EngineResponseDTO> getEngineWeight(float engineWeight) {
        return repository.findByEngineWeight(engineWeight).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackNoParam")
    public List<EngineResponseDTO> getAllEngines() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- Fallback methods (unique names) ---

    // For methods with Long + Throwable
    public EngineResponseDTO handleGetEngineFallback(Long id, Throwable t) {
        return serviceFallback.getEngineFallback(id, t);
    }

    public EngineResponseDTO handleGetEngineFallback(EngineRequestDTO dto, Throwable t) {
        return serviceFallback.getEngineFallback(-1L, t);
    }

    public EngineResponseDTO handleGetEngineFallback(Long id, EngineRequestDTO dto, Throwable t) {
        return serviceFallback.getEngineFallback(id, t);
    }

    // For methods with String parameter
    public List<EngineResponseDTO> handleGetEnginesFallbackString(String param, Throwable t) {
        return serviceFallback.getEnginesFallbackList(t);
    }

    // For methods with float parameter
    public List<EngineResponseDTO> handleGetEnginesFallbackFloat(float param, Throwable t) {
        return serviceFallback.getEnginesFallbackList(t);
    }

    // For methods with no parameters
    public List<EngineResponseDTO> handleGetEnginesFallbackNoParam(Throwable t) {
        return serviceFallback.getEnginesFallbackList(t);
    }
}