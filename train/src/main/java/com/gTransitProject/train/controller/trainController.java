package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.train;
import com.gTransitProject.train.service.trainService;
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
    @PostMapping
    public ResponseEntity<train> createTrain(@RequestBody train trainData) {
        train savedTrain = trainService.createTrain(trainData);
        return ResponseEntity.status(201).body(savedTrain);
    }

    @GetMapping
    public List<train> getAll() {
        return trainService.getAllTrains();
    }

    @GetMapping("/{id}")
    public ResponseEntity<train> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(trainService.getTrainById(id));
    }
}