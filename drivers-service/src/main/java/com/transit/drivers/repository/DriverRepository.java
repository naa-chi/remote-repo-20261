package com.transit.drivers.repository;

import com.transit.drivers.model.DriverModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<DriverModel, Long> {
    Optional<DriverModel> findByCode(String code);
}