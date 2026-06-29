package com.gTransitProject.typeengine.controller;

import com.gTransitProject.typeengine.assembler.engineModelAssembler;
import com.gTransitProject.typeengine.model.typeEngine;
import com.gTransitProject.typeengine.service.typeEngineService;
import com.gTransitProject.typeengine.typeEngineDTO.engineDTO;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/engines")
public class typeEngineController {

    @Autowired
    private typeEngineService service;

    @Autowired
    private engineModelAssembler assembler;

    @Operation(summary = "Create a new type of engine", description = "Creates a new typeEngine record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<EntityModel<engineDTO>> createTypeEngine(@RequestBody typeEngine typeEngineData) {
        typeEngine saved = service.createTypeEngine(typeEngineData);
        EntityModel<engineDTO> model = assembler.toModel(saved);
        URI location = linkTo(methodOn(typeEngineController.class).getById(saved.getId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "Get all types of engines", description = "Retrieves a list of all typeEngine records in the database.")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<engineDTO>>> getAll() {
        List<typeEngine> engines = service.getAll();
        List<EntityModel<engineDTO>> models = engines.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<engineDTO>> collection = CollectionModel.of(models,
                linkTo(methodOn(typeEngineController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Get a type of engine by ID", description = "Retrieves a specific typeEngine based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<engineDTO>> getById(@PathVariable Integer id) {
        typeEngine engine = service.getById(id);
        return ResponseEntity.ok(assembler.toModel(engine));
    }

    @Operation(summary = "Update an existing type of engine", description = "Updates an existing typeEngine record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<engineDTO>> updateTypeEngine(@PathVariable Integer id, @RequestBody typeEngine updatedTypeEngine) {
        typeEngine updated = service.updateTypeEngine(id, updatedTypeEngine);
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @Operation(summary = "Delete a type of engine", description = "Deletes an existing typeEngine record from the database.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeEngine(@PathVariable Integer id) {
        service.deleteTypeEngine(id);
        return ResponseEntity.noContent().build();
    }
}