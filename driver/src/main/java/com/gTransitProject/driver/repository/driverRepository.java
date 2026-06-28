package com.gTransitProject.driver.repository;

import com.gTransitProject.driver.model.driverModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface driverRepository extends JpaRepository<driverModel, Long> {
    // Custom query methods can be defined here if needed

}
