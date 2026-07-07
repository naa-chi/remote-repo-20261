package com.transit.tickets.service;

import com.transit.tickets.dto.mapper.TicketMapper;
import com.transit.tickets.dto.request.TicketRequestDTO;
import com.transit.tickets.dto.response.TicketResponseDTO;
import com.transit.tickets.fallback.TicketServiceFallback;
import com.transit.tickets.model.TicketModel;
import com.transit.tickets.repository.TicketsRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketsRepository repository;
    private final TicketMapper mapper;
    private final TicketServiceFallback fallback;

    public TicketService(TicketsRepository repository, TicketMapper mapper, TicketServiceFallback fallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.fallback = fallback;
    }

    @CircuitBreaker(name = "ticketService", fallbackMethod = "handleGetTicketsFallbackList")
    public List<TicketResponseDTO> getAllTickets() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "ticketService", fallbackMethod = "handleGetTicketFallback")
    public TicketResponseDTO getTicketById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + id));
    }

    @CircuitBreaker(name = "ticketService", fallbackMethod = "handleGetTicketByCodeFallback")
    public TicketResponseDTO getTicketByCode(String code) {
        return repository.findByCode(code).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Ticket not found with code: " + code));
    }

    @CircuitBreaker(name = "ticketService", fallbackMethod = "handleGetTicketsFallbackList")
    public List<TicketResponseDTO> getTicketsByCityCodeOrigin(String cityCodeOrigin) {
        return repository.findByCityCodeOrigin(cityCodeOrigin).stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "ticketService", fallbackMethod = "handleGetTicketsFallbackList")
    public List<TicketResponseDTO> getTicketsByCityCodeDestination(String cityCodeDestination) {
        return repository.findByCityCodeDestination(cityCodeDestination).stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "ticketService", fallbackMethod = "handleGetTicketFallback")
    public TicketResponseDTO createTicket(TicketRequestDTO request) {
        TicketModel model = mapper.toEntity(request);
        return mapper.toResponse(repository.save(model));
    }

    @CircuitBreaker(name = "ticketService", fallbackMethod = "handleGetTicketFallback")
    public TicketResponseDTO updateTicket(Long id, TicketRequestDTO request) {
        return repository.findById(id).map(existing -> {
            existing.setCode(request.getCode());
            existing.setCityCodeOrigin(request.getCityCodeOrigin());
            existing.setCityCodeDestination(request.getCityCodeDestination());
            existing.setPrice(request.getPrice());
            existing.setClientId(request.getClientId());
            existing.setTrainId(request.getTrainId());
            existing.setDepartureDate(request.getDepartureDate());
            return mapper.toResponse(repository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Ticket not found: " + id));
    }

    @CircuitBreaker(name = "ticketService", fallbackMethod = "handleGetTicketFallback")
    public void deleteTicket(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Ticket not found: " + id);
        repository.deleteById(id);
    }

    // Fallbacks
    private TicketResponseDTO handleGetTicketFallback(Long id, Throwable t) { return fallback.getTicketFallback(id, t); }
    private TicketResponseDTO handleGetTicketByCodeFallback(String code, Throwable t) { return fallback.getTicketByCodeFallback(code, t); }
    private List<TicketResponseDTO> handleGetTicketsFallbackList(Throwable t) { return fallback.getTicketsFallbackList(t); }
    private List<TicketResponseDTO> handleGetTicketsFallbackList(String param, Throwable t) { return fallback.getTicketsFallbackList(t); }
    private TicketResponseDTO handleGetTicketFallback(TicketRequestDTO request, Throwable t) { return fallback.getTicketFallback(-1L, t); }
    private TicketResponseDTO handleGetTicketFallback(Long id, TicketRequestDTO request, Throwable t) { return fallback.getTicketFallback(id, t); }
}