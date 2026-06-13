package com.gTransitProject.manufacturer.service;

import com.gTransitProject.manufacturer.model.manufacturerModel;
import com.gTransitProject.manufacturer.repo.manufacturerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor // Automatically injects the repo via constructor
public class manufacturerService {

    private final manufacturerRepo repo;

    public List<manufacturerModel> getAll() {
        return repo.findAll();
    }

    public manufacturerModel getById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Manufacturer not found with id: " + id));
    }

    public manufacturerModel getByName(String name) {
        return repo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Manufacturer not found with name: " + name));
    }

    public List<manufacturerModel> getByCountry(String country) {
        return repo.findByCountry(country);
    }

    public manufacturerModel create(manufacturerModel model) {
        return repo.save(model);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}