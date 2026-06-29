package com.gTransitProject.maintenance.controller;

import com.gTransitProject.maintenance.model.maintenanceModel;
import com.gTransitProject.maintenance.service.maintenanceService;

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
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
public class maintenanceController {

    private final maintenanceService service;
    private final maintenanceAssembler assembler;

    @Operation(summary = "Grabs every record there is.", description = "Calls a method that returns all stored maintenance records in the db.")
    @GetMapping
    public CollectionModel<EntityModel<maintenanceModel>> findAll() {
        log.info("Received request to fetch all maintenance records");
        return assembler.toCollectionModel(service.getAllMaintenance());
    }

    @Operation(summary = "Grabs ONE maintenance entry, using ID.", description = "Grabs one maintenance entry, using the unique ID, no others are returned.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<maintenanceModel>> findById(@PathVariable Integer id) {
        log.info("Received request to fetch maintenance record by id: {}", id);
        maintenanceModel record = service.getMaintenanceById(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(record));
    }

    @Operation(summary = "Grabs ONE maintenance entry, using vehicle ID.", description = "Grabs one maintenance entry, using the unique vehicle ID, no others are returned.")
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<EntityModel<maintenanceModel>> findByVehicleId(@PathVariable Integer vehicleId) {
        log.info("Received request to fetch maintenance record by vehicle ID: {}", vehicleId);
        maintenanceModel record = service.getMaintenanceByVehicleId(vehicleId);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(record));
    }

    @Operation(summary = "Creates a new entry.", description = "Creates a new entry using all the provided data.")
    @PostMapping
    public ResponseEntity<EntityModel<maintenanceModel>> save(@RequestBody maintenanceModel model) {
        log.info("Received request to create maintenance record: {}", model);
        maintenanceModel saved = service.createMaintenance(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Updates an existing entry.", description = "Updates an existing maintenance entry with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<maintenanceModel>> update(@PathVariable Integer id,
                                                                @RequestBody maintenanceModel model) {
        log.info("Received request to update maintenance record with id: {}", id);
        maintenanceModel updated = service.updateMaintenance(id, model);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }
}