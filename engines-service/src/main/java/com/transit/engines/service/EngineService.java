package com.transit.engines.service;

import com.transit.engines.assembler.EngineAssembler;
import com.transit.engines.dto.EngineDTO;
import com.transit.engines.fallback.EngineServiceFallback;
import com.transit.engines.model.EngineModel;
import com.transit.engines.repository.EnginesRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class EngineService {
    private final EnginesRepository repository;
    private final EngineAssembler assembler;
    private final EngineServiceFallback serviceFallback;

    public EngineService(EnginesRepository repository,
                         EngineAssembler assembler,
                         EngineServiceFallback serviceFallback) {
        this.repository = repository;
        this.assembler = assembler;
        this.serviceFallback = serviceFallback;
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineFallback")
    public EngineDTO getEngineById(Long id) {
        Optional<EngineModel> engineModelOptional = repository.findById(id);
        return engineModelOptional
            .map(assembler::toDTO)
            .orElseThrow(() -> new RuntimeException("Engine not found with id: " + id));
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesByManufacturerIdFallback") 
    public List<EngineDTO> getEnginesByManufacturerId(String manufacturerId) {
        List<EngineModel> engineModels = repository.findByManufacturerId(manufacturerId);
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found with id: " + manufacturerId);
        }
        return engineModels.stream()
            .map(assembler::toDTO)
            .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginesByEngineCodeFallback") 
    public List<EngineDTO> getEnginesByEngineCode(String engineCode) {
        List<EngineModel> engineModels = repository.findByEngineCode(engineCode);
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found with engine code: " + engineCode);
        }
        return engineModels.stream()
            .map(assembler::toDTO)
            .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineHorsepowerFallback") 
    public List<EngineDTO> getEngineHorsepower(float engineHorsepower) {
        List<EngineModel> engineModels = repository.findByEngineHorsepower(engineHorsepower);
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found with engine hp: " + engineHorsepower);
        }
        return engineModels.stream()
            .map(assembler::toDTO)
            .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEnginePriceFallback") 
    public List<EngineDTO> getEnginePrice(float enginePrice) {
        List<EngineModel> engineModels = repository.findByEnginePrice(enginePrice);
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found with engine price: " + enginePrice);
        }
        return engineModels.stream()
            .map(assembler::toDTO)
            .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineWeightFallback") 
    public List<EngineDTO> getEngineWeight(float engineWeight) {
        List<EngineModel> engineModels = repository.findByEngineWeight(engineWeight);
        if (engineModels.isEmpty()) {
            // FIXED: Typo corrected from "price" to "weight"
            throw new RuntimeException("No engines found with engine weight: " + engineWeight);
        }
        return engineModels.stream()
            .map(assembler::toDTO)
            .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetAllEnginesFallback")
    public List<EngineDTO> getAllEngines() {
        List<EngineModel> engineModels = repository.findAll();
        if (engineModels.isEmpty()) {
            throw new RuntimeException("No engines found");
        }

        return engineModels.stream()
            .map(assembler::toDTO)
            .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineFallback")
    public EngineDTO createEngine(EngineDTO engineDTO) {
        EngineModel model = assembler.toEntity(engineDTO);
        return assembler.toDTO(repository.save(model));
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineFallback")
    public EngineDTO updateEngine(Long id, EngineDTO engineDTO) {
        return repository.findById(id).map(existingEngine -> {
            // Update fields (Assuming assembler has toEntity or you map manually)
            EngineModel updated = assembler.toEntity(engineDTO);
            updated.setId(id); // Ensure the ID matches
            return assembler.toDTO(repository.save(updated));
        }).orElseThrow(() -> new RuntimeException("Engine not found with id: " + id));
    }

    @CircuitBreaker(name = "engineService", fallbackMethod = "handleGetEngineFallback")
    public void deleteEngine(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Engine not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // ==========================================
    // FALLBACK METHODS
    // ==========================================

    private EngineDTO handleGetEngineFallback(Long id, Throwable t) {
        return serviceFallback.getEngineFallback(id, t);
    }

    private List<EngineDTO> handleGetEnginesByManufacturerIdFallback(String manufacturerId, Throwable t) {
        // Ensure your EngineServiceFallback class has a matching method for this
        return serviceFallback.getEnginesByManufacturerIdFallback(manufacturerId, t);
    }

    private List<EngineDTO> handleGetEnginesByEngineCodeFallback(String engineCode, Throwable t) {
        return serviceFallback.getEnginesByEngineCodeFallback(engineCode, t);
    }

    private List<EngineDTO> handleGetEngineHorsepowerFallback(float engineHorsepower, Throwable t) {
        return serviceFallback.getEngineHorsepowerFallback(engineHorsepower, t);
    }

    private List<EngineDTO> handleGetEnginePriceFallback(float enginePrice, Throwable t) {
        return serviceFallback.getEnginePriceFallback(enginePrice, t);
    }

    private List<EngineDTO> handleGetEngineWeightFallback(float engineWeight, Throwable t) {
        return serviceFallback.getEngineWeightFallback(engineWeight, t);
    }

    private List<EngineDTO> handleGetAllEnginesFallback(Throwable t) {
        return serviceFallback.getAllEnginesFallback(t);
    }
}