package com.gTransitProject.driver.controller;

import com.gTransitProject.driver.model.driverModel;
import com.gTransitProject.driver.service.driverService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class driverController {

    @Autowired
    private driverService driverService;

    @Autowired
    private DriverAssembler assembler;

    @Operation(summary = "Get all drivers", description = "Retrieves a list of all drivers in the database.")
    @GetMapping
    public CollectionModel<EntityModel<driverModel>> getAllDrivers() {
        return assembler.toCollectionModel(driverService.getAllDrivers());
    }

    @Operation(summary = "Create a new driver", description = "Creates a new driver record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<EntityModel<driverModel>> createDriver(@RequestBody driverModel driver) {
        driverModel saved = driverService.createDriver(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Get a driver by ID", description = "Retrieves a specific driver based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<driverModel>> getDriverById(@PathVariable Long id) {
        driverModel driver = driverService.getDriverById(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(driver));
    }

    @Operation(summary = "Update a driver", description = "Updates an existing driver record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<driverModel>> updateDriver(@PathVariable Long id,
                                                                 @RequestBody driverModel driverDetails) {
        driverModel updated = driverService.updateDriver(id, driverDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @Operation(summary = "Delete a driver", description = "Deletes an existing driver record from the database based on its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}