package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.typeTrain;
import com.gTransitProject.train.service.typeTrainService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/typetrains")
public class typeTrainController {
    @Autowired
    private typeTrainService typeTrainService;

    @Operation(summary = "Create a new type of train", description = "Creates a new typeTrain record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<typeTrain> createTypeTrain(@RequestBody typeTrain typeTrainData) {
        typeTrain savedTypeTrain = typeTrainService.createTypeTrain(typeTrainData);
        return ResponseEntity.status(201).body(savedTypeTrain);
    }

    @Operation(summary = "Get all types of trains", description = "Retrieves a list of all typeTrain records in the database.")
    @GetMapping
    public List<typeTrain> getAll() {
        return typeTrainService.getAllTypeTrains();
    }

    @Operation(summary = "Get a type of train by ID", description = "Retrieves a specific typeTrain based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<typeTrain> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(typeTrainService.getTypeTrainByID(id));
    }
}
