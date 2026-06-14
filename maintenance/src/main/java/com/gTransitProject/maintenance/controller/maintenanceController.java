package com.gTransitProject.maintenance.controller;

import com.gTransitProject.maintenance.model.maintenanceModel;
import com.gTransitProject.maintenance.service.maintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class maintenanceController {
    private final maintenanceService service;

    @GetMapping
    public List<maintenanceModel> findAll() {
        return service.getAllMaintenance();
    }

    @GetMapping("/{id}")
    public ResponseEntity<maintenanceModel> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getMaintenanceById(id));
    }

    @PostMapping
    public maintenanceModel save(@RequestBody maintenanceModel model) {
        return service.createMaintenance(model);
    }
}
