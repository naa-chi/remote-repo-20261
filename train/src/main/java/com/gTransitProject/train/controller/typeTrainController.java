package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.typeTrain;
import com.gTransitProject.train.service.typeTrainService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainstype")
public class typeTrainController {

    @Autowired
    private typeTrainService typeTrainService;

    @Autowired
    private typeTrainAssembler assembler;

    @PostMapping
    public ResponseEntity<EntityModel<typeTrain>> createTypeTrain(@RequestBody typeTrain typeTrainData) {
        typeTrain saved = typeTrainService.createTypeTrain(typeTrainData);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @GetMapping
    public CollectionModel<EntityModel<typeTrain>> getAll() {
        return assembler.toCollectionModel(typeTrainService.getAllTypeTrains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<typeTrain>> getById(@PathVariable Integer id) {
        typeTrain found = typeTrainService.getTypeTrainByID(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(found));
    }
}