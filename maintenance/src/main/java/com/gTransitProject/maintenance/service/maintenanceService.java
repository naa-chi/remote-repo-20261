package com.gTransitProject.maintenance.service;

import com.gTransitProject.maintenance.model.maintenanceModel;
import com.gTransitProject.maintenance.repo.maintenanceRepo;
import com.gTransitProject.maintenance.exception.resourceNotFoundException;
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
                    return new resourceNotFoundException("Maintenance record not found with id: " + id);
                });
    }

    // --- NEW UPDATE METHOD ---
    public maintenanceModel updateMaintenance(Integer id, maintenanceModel updatedData) {
        log.info("Updating maintenance record with id: {}", id);
        
        // 1. Verify the record actually exists (this will throw your custom 404 if it doesn't)
        getMaintenanceById(id); 

        // 2. Ensure the ID from the URL is set on the object so JPA knows to UPDATE, not INSERT
        updatedData.setId(id); // Assumes your model has a setId() method (from Lombok @Data)

        // 3. Save it. Because the ID exists, Hibernate will run an UPDATE query.
        return maintenanceRepo.save(updatedData);
    }
}