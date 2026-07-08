package com.transit.trains.service;

import com.transit.trains.client.EngineClient;
import com.transit.trains.dto.mapper.TrainMapper;
import com.transit.trains.dto.request.TrainRequestDTO;
import com.transit.trains.dto.response.EngineResponseDTO;
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
    private final EngineClient engineClient;   // Feign client

    public TrainService(TrainsRepository repository,
                        TrainMapper mapper,
                        TrainServiceFallback serviceFallback,
                        EngineClient engineClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceFallback = serviceFallback;
        this.engineClient = engineClient;
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public TrainResponseDTO getTrainById(Long id) {
        TrainModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Train not found with id: " + id));
        TrainResponseDTO dto = mapper.toResponse(model);

        Long engineId = model.getEngineId();
        if (engineId != null) {
            try {
                EngineResponseDTO engine = engineClient.getEngineById(engineId);
            } catch (Exception e) {
                System.err.println("Failed to fetch engine " + engineId + ": " + e.getMessage());
            }
        }
        return dto;
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public TrainResponseDTO getTrainByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Train not found with code: " + code));
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainsFallbackList")
    public List<TrainResponseDTO> getTrainsByManufacturerId(String manufacturerId) {
        List<TrainModel> trainModels = repository.findByManufacturerId(manufacturerId);
        if (trainModels.isEmpty()) {
            throw new RuntimeException("No trains found with manufacturer ID: " + manufacturerId);
        }
        return trainModels.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainsFallbackList")
    public List<TrainResponseDTO> getAllTrains() {
        List<TrainModel> trainModels = repository.findAll();
        if (trainModels.isEmpty()) {
            throw new RuntimeException("No trains found in the database.");
        }
        return trainModels.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public void deleteTrain(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Train not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public TrainResponseDTO createTrain(TrainRequestDTO trainDTO) {
        TrainModel trainModel = mapper.toEntity(trainDTO);
        TrainModel savedTrain = repository.save(trainModel);
        return mapper.toResponse(savedTrain);
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainsFallbackList")
    public List<TrainResponseDTO> getTrainsByEngineId(Long engineId) {
        List<TrainModel> trains = repository.findByEngineId(engineId);
        if (trains.isEmpty()) {
            throw new RuntimeException("No trains found with engine ID: " + engineId);
        }
        return trains.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

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