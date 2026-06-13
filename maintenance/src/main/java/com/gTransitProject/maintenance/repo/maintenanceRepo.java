package com.gTransitProject.maintenance.repo;
import com.gTransitProject.maintenance.model.maintenanceModel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface maintenanceRepo extends JpaRepository<maintenanceModel, Integer>{
    

}
