package com.gTransitProject.manufacturer.controller;

import com.gTransitProject.manufacturer.model.manufacturerModel;
import com.gTransitProject.manufacturer.service.manufacturerService;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Gets all records", description = "Returns all manufacturers")
    @GetMapping
    public List<manufacturerModel> findAll() {
        log.info("Received request to fetch all manufacturers");
        return service.getAll();
    }

    @Operation(summary = "Get a product by id", description = "Returns a product as per the id")
    @GetMapping("/id/{id}")
    public ResponseEntity<manufacturerModel> findById(@PathVariable Integer id) {
        log.info("Received request to fetch manufacturer by id: {}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get a manufacturer by their name", description = "Returns a manufacturer as per the name")
    @GetMapping("/name/{name}")
    public ResponseEntity<manufacturerModel> findByName(@PathVariable String name) {
        log.info("Received request to fetch manufacturer by name: {}", name);
        return ResponseEntity.ok(service.getByName(name));
    }

    @Operation(summary = "Get manufacturers by country", description = "Returns a list of manufacturers from the specified country")
    @GetMapping("/country/{country}")
    public List<manufacturerModel> findByCountry(@PathVariable String country) {
        log.info("Received request to fetch manufacturers by country: {}", country);
        return service.getByCountry(country);
    }

    @Operation(summary = "Create a new manufacturer", description = "Creates a new manufacturer with the provided data")
    @PostMapping
    public manufacturerModel save(@RequestBody manufacturerModel model) {
        log.info("Received request to create manufacturer: {}", model);
        return service.create(model);
    }

    @Operation(summary = "Delete a manufacturer", description = "Deletes a manufacturer with the specified id")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Received request to delete manufacturer with id: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates an existing entry.", description = "Updates an existing maintenance entry with the provided data.")
    @PutMapping("/id/{id}")
    public ResponseEntity<manufacturerModel> update(@PathVariable Integer id, @RequestBody manufacturerModel model) {
        log.info("Received request to update manufacturer with id: {}", id);
        return ResponseEntity.ok(service.updateManufacturer(id, model));
    }
}