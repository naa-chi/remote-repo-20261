package com.transit.engines.service;

import com.transit.engines.dto.mapper.EngineMapper;
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
    private final EngineMapper mapper;
    private final EngineServiceFallback serviceFallback;

    public EngineService(EnginesRepository repository,
                         EngineMapper mapper,
                         EngineServiceFallback serviceFallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceFallback = serviceFallback;
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineFallback")
    public EngineResponseDTO getEngineById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Engine not found with id: " + id));
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackList")
    public List<EngineResponseDTO> getEnginesByManufacturerId(String manufacturerId) {
        List<EngineModel> engineModels = repository.findByManufacturerId(manufacturerId);
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found with manufacturer: " + manufacturerId);
        }
        return engineModels.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackList")
    public List<EngineResponseDTO> getEnginesByEngineCode(String engineCode) {
        List<EngineModel> engineModels = repository.findByEngineCode(engineCode);
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found with engine code: " + engineCode);
        }
        return engineModels.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackList")
    public List<EngineResponseDTO> getEngineHorsepower(float engineHorsepower) {
        List<EngineModel> engineModels = repository.findByEngineHorsepower(engineHorsepower);
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found with engine hp: " + engineHorsepower);
        }
        return engineModels.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackList")
    public List<EngineResponseDTO> getEnginePrice(float enginePrice) {
        List<EngineModel> engineModels = repository.findByEnginePrice(enginePrice);
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found with engine price: " + enginePrice);
        }
        return engineModels.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackList")
    public List<EngineResponseDTO> getEngineWeight(float engineWeight) {
        List<EngineModel> engineModels = repository.findByEngineWeight(engineWeight);
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found with engine weight: " + engineWeight);
        }
        return engineModels.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesFallbackList")
    public List<EngineResponseDTO> getAllEngines() {
        List<EngineModel> engineModels = repository.findAll();
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found");
        }
        return engineModels.stream().map(mapper::toResponse).collect(Collectors.toList());
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

    // Structural signatures for framework lookup compatibility
    public EngineResponseDTO handleGetEngineFallback(Long id, Throwable t) { return serviceFallback.getEngineFallback(id, t); }
    public EngineResponseDTO handleGetEngineFallback(EngineRequestDTO dto, Throwable t) { return serviceFallback.getEngineFallback(-1L, t); }
    public EngineResponseDTO handleGetEngineFallback(Long id, EngineRequestDTO dto, Throwable t) { return serviceFallback.getEngineFallback(id, t); }
    public List<EngineResponseDTO> handleGetEnginesFallbackList(Throwable t) { return serviceFallback.getEnginesFallbackList(t); }
    public List<EngineResponseDTO> handleGetEnginesFallbackList(String param, Throwable t) { return serviceFallback.getEnginesFallbackList(t); }
    public List<EngineResponseDTO> handleGetEnginesFallbackList(float param, Throwable t) { return serviceFallback.getEnginesFallbackList(t); }
}