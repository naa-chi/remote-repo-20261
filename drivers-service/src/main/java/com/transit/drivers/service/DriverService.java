package com.transit.drivers.service;

import com.transit.drivers.dto.mapper.DriverMapper;
import com.transit.drivers.dto.request.DriverRequestDTO;
import com.transit.drivers.dto.response.DriverResponseDTO;
import com.transit.drivers.fallback.DriverServiceFallback;
import com.transit.drivers.model.DriverModel;
import com.transit.drivers.repository.DriverRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepository repository;
    private final DriverMapper mapper;
    private final DriverServiceFallback fallback;

    public DriverService(DriverRepository repository,
                         DriverMapper mapper,
                         DriverServiceFallback fallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.fallback = fallback;
    }

    @CircuitBreaker(name = "driverService", fallbackMethod = "handleGetDriverFallback")
    public DriverResponseDTO getDriverById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
    }

    @CircuitBreaker(name = "driverService", fallbackMethod = "handleGetDriverByCodeFallback")
    public DriverResponseDTO getDriverByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Driver not found with code: " + code));
    }

    @CircuitBreaker(name = "driverService", fallbackMethod = "handleGetDriverFallback")
    public DriverResponseDTO createDriver(DriverRequestDTO request) {
        DriverModel entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @CircuitBreaker(name = "driverService", fallbackMethod = "handleGetDriverFallback")
    public DriverResponseDTO updateDriver(Long id, DriverRequestDTO request) {
        return repository.findById(id).map(existing -> {
            DriverModel updated = mapper.toEntity(request);
            updated.setId(id);
            return mapper.toResponse(repository.save(updated));
        }).orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
    }

    @CircuitBreaker(name = "driverService", fallbackMethod = "handleGetDriverFallback")
    public void deleteDriver(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Driver not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @CircuitBreaker(name = "driverService", fallbackMethod = "handleGetDriversFallbackList")
    public List<DriverResponseDTO> getAllDrivers() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // Fallback methods
    public DriverResponseDTO handleGetDriverFallback(Long id, Throwable t) {
        return fallback.getDriverFallback(id, t);
    }

    public DriverResponseDTO handleGetDriverByCodeFallback(String code, Throwable t) {
        return fallback.getDriverFallback(code, t);
    }

    public List<DriverResponseDTO> handleGetDriversFallbackList(Throwable t) {
        return fallback.getDriversFallbackList(t);
    }
}