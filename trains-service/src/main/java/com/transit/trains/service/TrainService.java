package com.transit.trains.service;

import com.transit.trains.dto.mapper.TrainMapper;
import com.transit.trains.dto.request.TrainRequestDTO;
import com.transit.trains.dto.response.TrainResponseDTO;
import com.transit.trains.fallback.TrainServiceFallback;
import com.transit.trains.model.TrainModel;
import com.transit.trains.repository.TrainsRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainService {
    private final TrainsRepository repository;
    private final TrainMapper mapper;
    private final TrainServiceFallback serviceFallback;

    public TrainService(TrainsRepository repository,
                        TrainMapper mapper,
                        TrainServiceFallback serviceFallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceFallback = serviceFallback;
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public TrainResponseDTO getTrainById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Train not found with id: " + id));
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public TrainResponseDTO getTrainByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Train not found with code: " + code));
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainsFallbackList")
    public List<TrainResponseDTO> getTrainsByManufacturerId(String manufacturerId) {
        return repository.findByManufacturerId(manufacturerId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainsFallbackList")
    public List<TrainResponseDTO> getAllTrains() {
        // Return empty list instead of throwing
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainsFallbackList")
    public List<TrainResponseDTO> getTrainsByEngineId(Long engineId) {
        // Return empty list instead of throwing
        return repository.findByEngineId(engineId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public TrainResponseDTO createTrain(TrainRequestDTO trainDTO) {
        TrainModel trainModel = mapper.toEntity(trainDTO);
        TrainModel savedTrain = repository.save(trainModel);
        return mapper.toResponse(savedTrain);
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public void deleteTrain(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Train not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // Fallback methods
    public TrainResponseDTO handleGetTrainFallback(Long id, Throwable t) {
        return serviceFallback.getTrainFallback(id, t);
    }

    public TrainResponseDTO handleGetTrainFallback(String code, Throwable t) {
        return serviceFallback.getTrainFallback(-1L, t);
    }

    public TrainResponseDTO handleGetTrainFallback(TrainRequestDTO trainDTO, Throwable t) {
        return serviceFallback.getTrainFallback(-1L, t);
    }

    public List<TrainResponseDTO> handleGetTrainsFallbackList(String manufacturerId, Throwable t) {
        return serviceFallback.getTrainsFallbackList(t);
    }

    public List<TrainResponseDTO> handleGetTrainsFallbackList(Throwable t) {
        return serviceFallback.getTrainsFallbackList(t);
    }
}