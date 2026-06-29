package com.gTransitProject.manufacturer.controller;

import com.gTransitProject.manufacturer.model.manufacturerModel;
import com.gTransitProject.manufacturer.service.manufacturerService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class manufacturerController {

    private final manufacturerService service;
    private final manufacturerAssembler assembler;

    @Operation(summary = "Gets all records", description = "Returns all manufacturers")
    @GetMapping
    public CollectionModel<EntityModel<manufacturerModel>> findAll() {
        log.info("Received request to fetch all manufacturers");
        return assembler.toCollectionModel(service.getAll());
    }

    @Operation(summary = "Get a product by id", description = "Returns a product as per the id")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<manufacturerModel>> findById(@PathVariable Integer id) {
        log.info("Received request to fetch manufacturer by id: {}", id);
        manufacturerModel manufacturer = service.getById(id);
        if (manufacturer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(manufacturer));
    }

    @Operation(summary = "Get a manufacturer by their name", description = "Returns a manufacturer as per the name")
    @GetMapping("/name/{name}")
    public ResponseEntity<EntityModel<manufacturerModel>> findByName(@PathVariable String name) {
        log.info("Received request to fetch manufacturer by name: {}", name);
        manufacturerModel manufacturer = service.getByName(name);
        if (manufacturer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(manufacturer));
    }

    @Operation(summary = "Get manufacturers by country", description = "Returns a list of manufacturers from the specified country")
    @GetMapping("/country/{country}")
    public CollectionModel<EntityModel<manufacturerModel>> findByCountry(@PathVariable String country) {
        log.info("Received request to fetch manufacturers by country: {}", country);
        List<manufacturerModel> manufacturers = service.getByCountry(country);
        return assembler.toCollectionModel(manufacturers);
    }

    @Operation(summary = "Create a new manufacturer", description = "Creates a new manufacturer with the provided data")
    @PostMapping
    public ResponseEntity<EntityModel<manufacturerModel>> save(@RequestBody manufacturerModel model) {
        log.info("Received request to create manufacturer: {}", model);
        manufacturerModel saved = service.create(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Delete a manufacturer", description = "Deletes a manufacturer with the specified id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Received request to delete manufacturer with id: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates an existing entry.", description = "Updates an existing maintenance entry with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<manufacturerModel>> update(@PathVariable Integer id,
                                                                 @RequestBody manufacturerModel model) {
        log.info("Received request to update manufacturer with id: {}", id);
        manufacturerModel updated = service.updateManufacturer(id, model);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }
}