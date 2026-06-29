package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.train;
import com.gTransitProject.train.service.trainService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trains")
public class trainController {

    @Autowired
    private trainService trainService;

    @Autowired
    private trainAssembler assembler;

    @PostMapping
    public ResponseEntity<EntityModel<train>> createTrain(@RequestBody train trainData) {
        train saved = trainService.createTrain(trainData);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @GetMapping
    public CollectionModel<EntityModel<train>> getAll() {
        return assembler.toCollectionModel(trainService.getAllTrains());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<EntityModel<train>> getTrainById(@PathVariable Integer id) {
        train found = trainService.getTrainById(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(found));
    }

    @GetMapping("/{code}")
    public ResponseEntity<EntityModel<train>> getByCode(@PathVariable String code) {
        train found = trainService.getTrainByCode(code);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(found));
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public ResponseEntity<EntityModel<train>> getByManufacturerId(@PathVariable Integer manufacturerId) {
        train found = trainService.getByManufacturerId(manufacturerId);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(found));
    }

    @GetMapping("/type/{typeTrainId}")
    public ResponseEntity<EntityModel<train>> getByTypeTrain(@PathVariable Integer typeTrainId) {
        train found = trainService.getByTypeTrain(typeTrainId);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(found));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<train>> updateTrain(@PathVariable Integer id, @RequestBody train updatedTrain) {
        train updated = trainService.updateTrain(id, updatedTrain);
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Integer id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }
}