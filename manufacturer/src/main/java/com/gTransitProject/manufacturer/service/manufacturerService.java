package com.gTransitProject.manufacturer.service;

import com.gTransitProject.manufacturer.model.manufacturerModel;
import com.gTransitProject.manufacturer.repo.manufacturerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j // Lombok annotation to auto-generate the logger
@Service
@RequiredArgsConstructor 
public class manufacturerService {

    private final manufacturerRepo repo;

    public List<manufacturerModel> getAll() {
        log.info("Fetching all manufacturers");
        return repo.findAll();
    }

    public manufacturerModel getById(Integer id) {
        log.info("Fetching manufacturer with id: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to find manufacturer with id: {}", id);
                    return new RuntimeException("Manufacturer not found with id: " + id);
                });
    }

    public manufacturerModel getByName(String name) {
        log.info("Fetching manufacturer by name: {}", name);
        return repo.findByName(name)
                .orElseThrow(() -> {
                    log.error("Failed to find manufacturer with name: {}", name);
                    return new RuntimeException("Manufacturer not found with name: " + name);
                });
    }

    public List<manufacturerModel> getByCountry(String country) {
        log.info("Fetching manufacturers from country: {}", country);
        return repo.findByCountry(country);
    }

    public manufacturerModel create(manufacturerModel model) {
        log.info("Creating new manufacturer: {}", model);
        // Note: The above assumes manufacturerModel has a standard toString() 
        // (which Lombok's @Data or @Value provides).
        return repo.save(model);
    }

    public void delete(Integer id) {
        log.info("Deleting manufacturer with id: {}", id);
        repo.deleteById(id);
    }
}