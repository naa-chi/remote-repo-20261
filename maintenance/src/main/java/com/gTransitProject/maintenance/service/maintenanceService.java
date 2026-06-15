package com.gTransitProject.maintenance.service;

import com.gTransitProject.maintenance.model.maintenanceModel;
import com.gTransitProject.maintenance.repo.maintenanceRepo;
import com.gTransitProject.maintenance.exception.resourceNotFoundException; // Ensure this matches your package structure
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class maintenanceService {
    
    private final maintenanceRepo maintenanceRepo;

    public maintenanceModel createMaintenance(maintenanceModel maintenanceData) {
        log.info("Creating new maintenance record: {}", maintenanceData);
        return maintenanceRepo.save(maintenanceData);
    }

    public List<maintenanceModel> getAllMaintenance() {
        log.info("Fetching all maintenance records");
        return maintenanceRepo.findAll();
    }

    public maintenanceModel getMaintenanceById(Integer id) {
        log.info("Fetching maintenance record with id: {}", id);
        return maintenanceRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to find maintenance record with id: {}", id);
                    // This now triggers your GlobalExceptionHandler automatically
                    return new resourceNotFoundException("Maintenance record not found with id: " + id);
                });
    }
}