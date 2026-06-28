package com.gTransitProject.driver.controller;
import com.gTransitProject.driver.model.driverModel;
import com.gTransitProject.driver.service.driverService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class driverController {
    
    @Autowired
    private driverService driverService;

    @Operation(summary = "Get all drivers", description = "Retrieves a list of all drivers in the database.")
    @GetMapping
    public ResponseEntity<List<driverModel>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @Operation(summary = "Create a new driver", description = "Creates a new driver record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<driverModel> createDriver(@RequestBody driverModel driver) {
        return ResponseEntity.ok(driverService.createDriver(driver));
    }

    @Operation(summary = "Get a driver by ID", description = "Retrieves a specific driver based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<driverModel> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @Operation(summary = "Update a driver", description = "Updates an existing driver record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<driverModel> updateDriver(@PathVariable Long id, @RequestBody driverModel driverDetails) {
        return ResponseEntity.ok(driverService.updateDriver(id, driverDetails));
    }

    @Operation(summary = "Delete a driver", description = "Deletes an existing driver record from the database based on its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}
