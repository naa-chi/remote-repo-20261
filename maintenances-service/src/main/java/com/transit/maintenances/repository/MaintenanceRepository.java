package com.transit.maintenances.repository;

import com.transit.maintenances.model.MaintenanceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceModel, Long> {
    // ✅ Fixed method name to match the entity field (maintenaceCrewGroup)
    List<MaintenanceModel> findByMaintenanceCrewGroup(String maintenaceCrewGroup);
}