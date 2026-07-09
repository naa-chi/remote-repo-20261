package com.transit.lines.service;

import com.transit.lines.dto.mapper.LineMapper;
import com.transit.lines.dto.request.LineRequestDTO;
import com.transit.lines.dto.response.LineResponseDTO;
import com.transit.lines.fallback.LineServiceFallback;
import com.transit.lines.model.LineModel;
import com.transit.lines.repository.LineRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository repository;
    private final LineMapper mapper;
    private final LineServiceFallback fallback;

    public LineService(LineRepository repository, LineMapper mapper, LineServiceFallback fallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.fallback = fallback;
    }

    @CircuitBreaker(name = "lineService", fallbackMethod = "handleGetLineFallback")
    public LineResponseDTO getLineById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Line not found with id: " + id));
    }

    @CircuitBreaker(name = "lineService", fallbackMethod = "handleGetLineByCodeFallback")
    public LineResponseDTO getLineByCode(Integer lineCode) {
        return repository.findByLineCode(lineCode)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Line not found with code: " + lineCode));
    }

    @CircuitBreaker(name = "lineService", fallbackMethod = "handleGetLineFallback")
    public LineResponseDTO createLine(LineRequestDTO request) {
        LineModel entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @CircuitBreaker(name = "lineService", fallbackMethod = "handleGetLineFallback")
    public LineResponseDTO updateLine(Long id, LineRequestDTO request) {
        return repository.findById(id).map(existing -> {
            LineModel updated = mapper.toEntity(request);
            updated.setId(id);
            return mapper.toResponse(repository.save(updated));
        }).orElseThrow(() -> new RuntimeException("Line not found with id: " + id));
    }

    @CircuitBreaker(name = "lineService", fallbackMethod = "handleGetLineFallback")
    public void deleteLine(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Line not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @CircuitBreaker(name = "lineService", fallbackMethod = "handleGetLinesFallbackList")
    public List<LineResponseDTO> getAllLines() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // Fallback methods
    public LineResponseDTO handleGetLineFallback(Long id, Throwable t) {
        return fallback.getLineFallback(id, t);
    }

    public LineResponseDTO handleGetLineByCodeFallback(Integer code, Throwable t) {
        return fallback.getLineFallback(code, t);
    }

    public List<LineResponseDTO> handleGetLinesFallbackList(Throwable t) {
        return fallback.getLinesFallbackList(t);
    }
}