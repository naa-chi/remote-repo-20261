package com.transit.clients.service;

import com.transit.clients.dto.mapper.ClientMapper;
import com.transit.clients.dto.request.ClientRequestDTO;
import com.transit.clients.dto.response.ClientResponseDTO;
import com.transit.clients.fallback.ClientServiceFallback;
import com.transit.clients.model.ClientModel;
import com.transit.clients.repository.ClientRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;
    private final ClientServiceFallback fallback;

    public ClientService(ClientRepository repository,
                         ClientMapper mapper,
                         ClientServiceFallback fallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.fallback = fallback;
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "handleGetClientFallback")
    public ClientResponseDTO getClientById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "handleGetClientByCodeFallback")
    public ClientResponseDTO getClientByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Client not found with code: " + code));
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "handleGetClientFallback")
    public ClientResponseDTO createClient(ClientRequestDTO request) {
        ClientModel entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "handleGetClientFallback")
    public ClientResponseDTO updateClient(Long id, ClientRequestDTO request) {
        return repository.findById(id).map(existing -> {
            ClientModel updated = mapper.toEntity(request);
            updated.setId(id);
            return mapper.toResponse(repository.save(updated));
        }).orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "handleGetClientFallback")
    public void deleteClient(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "handleGetClientsFallbackList")
    public List<ClientResponseDTO> getAllClients() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // Fallback methods
    public ClientResponseDTO handleGetClientFallback(Long id, Throwable t) {
        return fallback.getClientFallback(id, t);
    }

    public ClientResponseDTO handleGetClientByCodeFallback(String code, Throwable t) {
        return fallback.getClientFallback(code, t);
    }

    public List<ClientResponseDTO> handleGetClientsFallbackList(Throwable t) {
        return fallback.getClientsFallbackList(t);
    }
}