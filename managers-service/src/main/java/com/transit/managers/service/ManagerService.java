package com.transit.managers.service;

import com.transit.managers.dto.mapper.ManagerMapper;
import com.transit.managers.dto.request.ManagerRequestDTO;
import com.transit.managers.dto.response.ManagerResponseDTO;
import com.transit.managers.fallback.ManagerServiceFallback;
import com.transit.managers.model.ManagerModel;
import com.transit.managers.repository.ManagerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final ManagerRepository repository;
    private final ManagerMapper mapper;
    private final ManagerServiceFallback fallback;

    public ManagerService(ManagerRepository repository,
                          ManagerMapper mapper,
                          ManagerServiceFallback fallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.fallback = fallback;
    }

    @CircuitBreaker(name = "managerService", fallbackMethod = "handleGetManagerFallback")
    public ManagerResponseDTO getManagerById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + id));
    }

    @CircuitBreaker(name = "managerService", fallbackMethod = "handleGetManagerByCodeFallback")
    public ManagerResponseDTO getManagerByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Manager not found with code: " + code));
    }

    @CircuitBreaker(name = "managerService", fallbackMethod = "handleGetManagerFallback")
    public ManagerResponseDTO createManager(ManagerRequestDTO request) {
        ManagerModel entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @CircuitBreaker(name = "managerService", fallbackMethod = "handleGetManagerFallback")
    public ManagerResponseDTO updateManager(Long id, ManagerRequestDTO request) {
        return repository.findById(id).map(existing -> {
            ManagerModel updated = mapper.toEntity(request);
            updated.setId(id);
            return mapper.toResponse(repository.save(updated));
        }).orElseThrow(() -> new RuntimeException("Manager not found with id: " + id));
    }

    @CircuitBreaker(name = "managerService", fallbackMethod = "handleGetManagerFallback")
    public void deleteManager(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Manager not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @CircuitBreaker(name = "managerService", fallbackMethod = "handleGetManagersFallbackList")
    public List<ManagerResponseDTO> getAllManagers() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // Fallback methods
    public ManagerResponseDTO handleGetManagerFallback(Long id, Throwable t) {
        return fallback.getManagerFallback(id, t);
    }

    public ManagerResponseDTO handleGetManagerByCodeFallback(String code, Throwable t) {
        return fallback.getManagerFallback(code, t);
    }

    public List<ManagerResponseDTO> handleGetManagersFallbackList(Throwable t) {
        return fallback.getManagersFallbackList(t);
    }
}