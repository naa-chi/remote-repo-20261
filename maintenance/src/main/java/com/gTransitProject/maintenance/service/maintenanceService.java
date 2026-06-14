package com.gTransitProject.maintenance.service;
import com.gTransitProject.maintenance.model.maintenanceModel;
import com.gTransitProject.maintenance.repo.maintenanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class maintenanceService {
    @Autowired
    private maintenanceRepo maintenanceRepo;

    public maintenanceModel createMaintenance(maintenanceModel maintenanceData) {
        return maintenanceRepo.save(maintenanceData);
    }

    public List<maintenanceModel> getAllMaintenance() {
        return maintenanceRepo.findAll();
    }

    public maintenanceModel getMaintenanceById(Integer id) {
        return maintenanceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance record not found"));
    }
}
