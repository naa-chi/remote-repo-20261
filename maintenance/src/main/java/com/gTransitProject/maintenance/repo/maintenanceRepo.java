package com.gTransitProject.maintenance.repo;

import com.gTransitProject.maintenance.model.maintenanceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface maintenanceRepo extends JpaRepository<maintenanceModel, Integer> {
    // JpaRepository gives you built-in save(), findById(), findAll(), deleteById(), etc.
}