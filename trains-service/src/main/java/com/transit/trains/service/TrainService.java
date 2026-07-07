package com.transit.trains.service;

import com.transit.trains.assembler.TrainAssembler;
import com.transit.trains.dto.TrainDTO;
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
    private final TrainAssembler assembler;
    private final TrainServiceFallback serviceFallback;

    public TrainService(TrainsRepository repository,
                        TrainAssembler assembler,
                        TrainServiceFallback serviceFallback) {
        this.repository = repository;
        this.assembler = assembler;
        this.serviceFallback = serviceFallback;
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public TrainDTO getTrainById(Long id) {
        return repository.findById(id)
                .map(assembler::toDTO)
                .orElseThrow(() -> new RuntimeException("Train not found with id: " + id));
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public TrainDTO getTrainByCode(String code) {
        // FIXED: Uses the efficient repository method instead of fetching all
        return repository.findByCode(code)
                .map(assembler::toDTO)
                .orElseThrow(() -> new RuntimeException("Train not found with code: " + code));
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public List<TrainDTO> getTrainsByManufacturerId(String manufacturerId) {
        List<TrainModel> trainModels = repository.findByManufacturerId(manufacturerId);
        if (trainModels.isEmpty()) {
            throw new RuntimeException("No trains found with manufacturer ID: " + manufacturerId);
        }
        return trainModels.stream()
                .map(assembler::toDTO)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "trainService", fallbackMethod = "handleGetTrainFallback")
    public List<TrainDTO> getAllTrains() {
        List<TrainModel> trainModels = repository.findAll();
        if (trainModels.isEmpty()) {
            throw new RuntimeException("No trains found in the database.");
        }
        return trainModels.stream()
                .map(assembler::toDTO)
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
    public TrainDTO createTrain(TrainDTO trainDTO) {
        TrainModel trainModel = assembler.toEntity(trainDTO);
        TrainModel savedTrain = repository.save(trainModel);
        return assembler.toDTO(savedTrain);
    }

    // Fallback handler (must match signature)
    public TrainDTO handleGetTrainFallback(Long id, Throwable t) {
        return serviceFallback.getTrainFallback(id, t);
    }
}