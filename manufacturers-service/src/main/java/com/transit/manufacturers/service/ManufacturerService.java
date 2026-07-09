package com.transit.manufacturers.service;

import com.transit.manufacturers.dto.mapper.ManufacturerMapper;
import com.transit.manufacturers.dto.request.ManufacturerRequestDTO;
import com.transit.manufacturers.dto.response.ManufacturerResponseDTO;
import com.transit.manufacturers.fallback.ManufacturerServiceFallback;
import com.transit.manufacturers.model.ManufacturerModel;
import com.transit.manufacturers.repository.ManufacturerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManufacturerService {

    private final ManufacturerRepository repository;
    private final ManufacturerMapper mapper;
    private final ManufacturerServiceFallback fallback;

    public ManufacturerService(ManufacturerRepository repository,
                               ManufacturerMapper mapper,
                               ManufacturerServiceFallback fallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.fallback = fallback;
    }

    @CircuitBreaker(name = "manufacturerService", fallbackMethod = "handleGetManufacturerFallback")
    public ManufacturerResponseDTO getManufacturerById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Manufacturer not found with id: " + id));
    }

    @CircuitBreaker(name = "manufacturerService", fallbackMethod = "handleGetManufacturerByCodeFallback")
    public ManufacturerResponseDTO getManufacturerByCode(String manufacturerId) {
        return repository.findByManufacturerId(manufacturerId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Manufacturer not found with code: " + manufacturerId));
    }

    @CircuitBreaker(name = "manufacturerService", fallbackMethod = "handleGetManufacturerFallback")
    public ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO request) {
        ManufacturerModel entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @CircuitBreaker(name = "manufacturerService", fallbackMethod = "handleGetManufacturerFallback")
    public ManufacturerResponseDTO updateManufacturer(Long id, ManufacturerRequestDTO request) {
        return repository.findById(id).map(existing -> {
            ManufacturerModel updated = mapper.toEntity(request);
            updated.setId(id);
            return mapper.toResponse(repository.save(updated));
        }).orElseThrow(() -> new RuntimeException("Manufacturer not found with id: " + id));
    }

    @CircuitBreaker(name = "manufacturerService", fallbackMethod = "handleGetManufacturerFallback")
    public void deleteManufacturer(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Manufacturer not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @CircuitBreaker(name = "manufacturerService", fallbackMethod = "handleGetManufacturersFallbackList")
    public List<ManufacturerResponseDTO> getAllManufacturers() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // Fallback methods
    public ManufacturerResponseDTO handleGetManufacturerFallback(Long id, Throwable t) {
        return fallback.getManufacturerFallback(id, t);
    }

    public ManufacturerResponseDTO handleGetManufacturerByCodeFallback(String code, Throwable t) {
        return fallback.getManufacturerFallback(code, t);
    }

    public List<ManufacturerResponseDTO> handleGetManufacturersFallbackList(Throwable t) {
        return fallback.getManufacturersFallbackList(t);
    }
}