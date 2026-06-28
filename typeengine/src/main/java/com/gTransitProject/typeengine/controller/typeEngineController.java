package com.gTransitProject.typeengine.controller;

import com.gTransitProject.typeengine.model.typeEngine;
import com.gTransitProject.typeengine.service.typeEngineService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/engine")
public class typeEngineController {

    @Autowired
    private typeEngineService service;

    @Operation(summary = "Create a new type of engine", description = "Creates a new typeEngine record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<typeEngine> createTypeEngine(@RequestBody typeEngine typeEngineData) {
        typeEngine savedTypeEngine = service.createTypeEngine(typeEngineData);
        return ResponseEntity.status(201).body(savedTypeEngine);
    }

    @Operation(summary = "Get all types of engines", description = "Retrieves a list of all typeEngine records in the database.")
    @GetMapping
    public List<typeEngine> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Get a type of engine by ID", description = "Retrieves a specific typeEngine based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<typeEngine> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Update an existing type of engine", description = "Updates an existing typeEngine record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<typeEngine> updateTypeEngine(@PathVariable Integer id, @RequestBody typeEngine updatedTypeEngine) {
        return ResponseEntity.ok(service.updateTypeEngine(id, updatedTypeEngine));
    }

    @Operation(summary = "Delete a type of engine", description = "Deletes an existing typeEngine record from the database.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeEngine(@PathVariable Integer id) {
        service.deleteTypeEngine(id);
        return ResponseEntity.noContent().build();
    }
}
