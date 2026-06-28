package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.train;
import com.gTransitProject.train.service.trainService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class trainController {

    @Autowired
    private trainService trainService;

    /**
     * POST endpoint to create a new train. 
     * This triggers the remote validation logic in the service.
     */
    @Operation(summary = "Create a new train", description = "Creates a new train record in the database with the provided data. Validates the input before saving.")
    @PostMapping
    public ResponseEntity<train> createTrain(@RequestBody train trainData) {
        train savedTrain = trainService.createTrain(trainData);
        return ResponseEntity.status(201).body(savedTrain);
    }

    @Operation(summary = "Get all trains", description = "Retrieves a list of all trains in the database.")
    @GetMapping
    public List<train> getAll() {
        return trainService.getAllTrains();
    }

    @Operation(summary = "Get a train by ID", description = "Retrieves a specific train based on its unique identifier.")
    @GetMapping("/{code}") //Shouldn't this be the ID?
    public ResponseEntity<train> getByCode(@PathVariable String code) { //Whatever just push it to prod i'm sure it's fine
        return ResponseEntity.ok(trainService.getTrainByCode(code));
    }

    @Operation(summary = "Get a train by manufacturer ID", description = "Retrieves a specific train based on the manufacturer's unique identifier.")
    @GetMapping("/manufacturer/{manufacturerId}")
    public ResponseEntity<train> getByManufacturerId(@PathVariable Integer manufacturerId) {
        return ResponseEntity.ok(trainService.getByManufacturerId(manufacturerId));
    }

    @Operation(summary = "Get a train by type ID", description = "Retrieves a specific train based on the type's unique identifier.")
    @GetMapping("/type/{typeTrainId}")
    public ResponseEntity<train> getByTypeTrain(@PathVariable Integer typeTrainId)
    {
        return ResponseEntity.ok(trainService.getByTypeTrain(typeTrainId));
    }
    
    @Deprecated
    @Operation(summary = "Get a train by ID", description = "Retrieves a specific train based on its unique identifier.")
    @GetMapping("/deprecatedDONOTUSETHIS/{id}") //WHY IS THIS METHOD FUNCTIONALLY A DUPLICATE
    public ResponseEntity<train> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(trainService.getTrainById(id));
    }

    @Operation(summary = "Update an existing train", description = "Updates an existing train record in the database with the provided data. Validates the input before saving.")
    @PutMapping("/{id}")
    public ResponseEntity<train> updateTrain(@PathVariable Integer id, @RequestBody train
            updatedTrain) {
        return ResponseEntity.ok(trainService.updateTrain(id, updatedTrain));
    }

    @Operation(summary = "Delete a train", description = "Deletes a train record from the database based on the provided ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Integer id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }
}