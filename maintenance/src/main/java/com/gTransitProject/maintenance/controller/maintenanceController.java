package com.gTransitProject.maintenance.controller;

import com.gTransitProject.maintenance.model.maintenanceModel;
import com.gTransitProject.maintenance.service.maintenanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class maintenanceController {
    
    private final maintenanceService service;

    @GetMapping
    public List<maintenanceModel> findAll() {
        log.info("Received request to fetch all maintenance records");
        return service.getAllMaintenance();
    }

    @GetMapping("/{id}")
    public ResponseEntity<maintenanceModel> findById(@PathVariable Integer id) {
        log.info("Received request to fetch maintenance record by id: {}", id);
        return ResponseEntity.ok(service.getMaintenanceById(id));
    }

    @PostMapping
    public maintenanceModel save(@RequestBody maintenanceModel model) {
        log.info("Received request to create maintenance record: {}", model);
        return service.createMaintenance(model);
    }

    // --- NEW UPDATE ENDPOINT ---
    @PutMapping("/{id}")
    public ResponseEntity<maintenanceModel> update(@PathVariable Integer id, @RequestBody maintenanceModel model) {
        log.info("Received request to update maintenance record with id: {}", id);
        return ResponseEntity.ok(service.updateMaintenance(id, model));
    }
}