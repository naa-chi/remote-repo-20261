package com.gTransitProject.manufacturer.controller;

import com.gTransitProject.manufacturer.model.manufacturerModel;
import com.gTransitProject.manufacturer.service.manufacturerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // Brings in the logger
@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class manufacturerController {

    private final manufacturerService service;

    @GetMapping
    public List<manufacturerModel> findAll() {
        log.info("Received request to fetch all manufacturers");
        return service.getAll();
    }

    @GetMapping("/searchId/{id}")
    public ResponseEntity<manufacturerModel> findById(@PathVariable Integer id) {
        log.info("Received request to fetch manufacturer by id: {}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/searchName/{name}")
    public ResponseEntity<manufacturerModel> findByName(@PathVariable String name) {
        log.info("Received request to fetch manufacturer by name: {}", name);
        return ResponseEntity.ok(service.getByName(name));
    }

    @GetMapping("/searchCountry/{country}")
    public List<manufacturerModel> findByCountry(@PathVariable String country) {
        log.info("Received request to fetch manufacturers by country: {}", country);
        return service.getByCountry(country);
    }

    @PostMapping
    public manufacturerModel save(@RequestBody manufacturerModel model) {
        log.info("Received request to create manufacturer: {}", model);
        return service.create(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Received request to delete manufacturer with id: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}