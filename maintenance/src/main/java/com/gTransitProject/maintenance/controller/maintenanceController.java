package com.gTransitProject.maintenance.controller;

import com.gTransitProject.maintenance.model.maintenanceModel;
import com.gTransitProject.maintenance.service.maintenanceService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
@Operation(summary = " Placeholder (should be short)", description = "Actual proper description here with lengthy stuff")
*/

@Slf4j
@RestController
@RequestMapping("/api/maintenances") //doesn't make sense to be plural here but we gotta be consistent at some point
@RequiredArgsConstructor
public class maintenanceController {
    
    private final maintenanceService service;

    @Operation(summary = "Grabs every record there is.", description = "Calls a method that returns all stored maintenance records in the db. ")
    @GetMapping
    public List<maintenanceModel> findAll() {
        log.info("Received request to fetch all maintenance records");
        return service.getAllMaintenance();
    }

    @Operation(summary = "Grabs ONE maintenance entry, using ID.", description = "Grabs one maintenance entry, using the unique ID, no others are returned.")
    @GetMapping("/{id}")
    public ResponseEntity<maintenanceModel> findById(@PathVariable Integer id) {
        log.info("Received request to fetch maintenance record by id: {}", id);
        return ResponseEntity.ok(service.getMaintenanceById(id));
    }

    @Operation(summary = "Grabs ONE maintenance entry, using vehicle ID.", description = "Grabs one maintenance entry, using the unique vehicle ID, no others are returned.")
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<maintenanceModel> findByVehicleId(@PathVariable Integer vehicleId) {
        log.info("Received request to fetch maintenance record by vehicle ID: {}", vehicleId);
        return ResponseEntity.ok(service.getMaintenanceByVehicleId(vehicleId));
    }

    @Operation(summary = "Creates a new entry.", description = "Creates a new entry used all the provided data.")
    @PostMapping
    public maintenanceModel save(@RequestBody maintenanceModel model) {
        log.info("Received request to create maintenance record: {}", model);
        return service.createMaintenance(model);
    }

    @Operation(summary = "Updates an existing entry.", description = "Updates an existing maintenance entry with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<maintenanceModel> update(@PathVariable Integer id, @RequestBody maintenanceModel model) {
        log.info("Received request to update maintenance record with id: {}", id);
        return ResponseEntity.ok(service.updateMaintenance(id, model));
    }
    //seems like this is all we need for a put endpoint i suppose
}