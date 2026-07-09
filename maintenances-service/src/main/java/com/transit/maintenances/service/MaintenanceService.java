package com.transit.maintenances.service;

import com.transit.maintenances.dto.mapper.MaintenanceMapper;
import com.transit.maintenances.dto.request.MaintenanceRequestDTO;
import com.transit.maintenances.dto.response.MaintenanceResponseDTO;
import com.transit.maintenances.fallback.MaintenanceServiceFallback;
import com.transit.maintenances.model.MaintenanceModel;
import com.transit.maintenances.repository.MaintenanceRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    private final MaintenanceRepository repository;
    private final MaintenanceMapper mapper;
    private final MaintenanceServiceFallback serviceFallback;

    public MaintenanceService(MaintenanceRepository repository,
                              MaintenanceMapper mapper,
                              MaintenanceServiceFallback serviceFallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceFallback = serviceFallback;
    }

    // --- Single item methods ---

    @CircuitBreaker(name = "maintenanceService", fallbackMethod = "handleGetMaintenanceFallback")
    public MaintenanceResponseDTO getMaintenanceById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));
    }

    @CircuitBreaker(name = "maintenanceService", fallbackMethod = "handleGetMaintenanceByCodeFallback")
    public MaintenanceResponseDTO getMaintenanceByCode(String maintenanceId) {
        return repository.findAll().stream()
                .filter(m -> m.getMaintenanceId().equals(maintenanceId))
                .findFirst()
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with code: " + maintenanceId));
    }

    @CircuitBreaker(name = "maintenanceService", fallbackMethod = "handleGetMaintenanceFallback")
    public MaintenanceResponseDTO createMaintenance(MaintenanceRequestDTO requestDTO) {
        MaintenanceModel model = mapper.toEntity(requestDTO);
        return mapper.toResponse(repository.save(model));
    }

    @CircuitBreaker(name = "maintenanceService", fallbackMethod = "handleGetMaintenanceFallback")
    public MaintenanceResponseDTO updateMaintenance(Long id, MaintenanceRequestDTO requestDTO) {
        return repository.findById(id).map(existing -> {
            MaintenanceModel updated = mapper.toEntity(requestDTO);
            updated.setId(id);
            return mapper.toResponse(repository.save(updated));
        }).orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));
    }

    @CircuitBreaker(name = "maintenanceService", fallbackMethod = "handleGetMaintenanceFallback")
    public void deleteMaintenance(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Maintenance not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // --- List methods (return empty list instead of throwing) ---

    @CircuitBreaker(name = "maintenanceService", fallbackMethod = "handleGetMaintenancesFallbackList")
    public List<MaintenanceResponseDTO> getAllMaintenances() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "maintenanceService", fallbackMethod = "handleGetMaintenancesFallbackList")
    public List<MaintenanceResponseDTO> getMaintenancesByCrewId(String crewId) {
        return repository.findByMaintenanceCrewGroup(crewId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- Fallback methods (unique names) ---

    // For methods with Long + Throwable
    public MaintenanceResponseDTO handleGetMaintenanceFallback(Long id, Throwable t) {
        return serviceFallback.getMaintenanceFallback(id, t);
    }

    public MaintenanceResponseDTO handleGetMaintenanceFallback(MaintenanceRequestDTO request, Throwable t) {
        return serviceFallback.getMaintenanceFallback(-1L, t);
    }

    public MaintenanceResponseDTO handleGetMaintenanceFallback(Long id, MaintenanceRequestDTO request, Throwable t) {
        return serviceFallback.getMaintenanceFallback(id, t);
    }

    // For methods with String parameter
    public MaintenanceResponseDTO handleGetMaintenanceByCodeFallback(String maintenanceId, Throwable t) {
        return serviceFallback.getMaintenanceFallback(maintenanceId, t);
    }

    // For methods with no parameters or String list parameters
    public List<MaintenanceResponseDTO> handleGetMaintenancesFallbackList(Throwable t) {
        return serviceFallback.getMaintenancesFallbackList(t);
    }

    public List<MaintenanceResponseDTO> handleGetMaintenancesFallbackList(String param, Throwable t) {
        return serviceFallback.getMaintenancesFallbackList(t);
    }
}