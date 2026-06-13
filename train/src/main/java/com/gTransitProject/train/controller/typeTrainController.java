package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.typeTrain;
import com.gTransitProject.train.service.typeTrainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/typeTrains")
public class typeTrainController {
    @Autowired
    private typeTrainService typeTrainService;

    @PostMapping
    public ResponseEntity<typeTrain> createTypeTrain(@RequestBody typeTrain typeTrainData) {
        typeTrain savedTypeTrain = typeTrainService.createTypeTrain(typeTrainData);
        return ResponseEntity.status(201).body(savedTypeTrain);
    }

    @GetMapping
    public List<typeTrain> getAll() {
        return typeTrainService.getAllTypeTrains();
    }

    @GetMapping("/{id}")
    public ResponseEntity<typeTrain> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(typeTrainService.getTypeTrainByID(id));
    }
}
