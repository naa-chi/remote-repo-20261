package com.transit.manufacturers.repository;

import com.transit.manufacturers.model.ManufacturerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<ManufacturerModel, Long> {
    Optional<ManufacturerModel> findByManufacturerId(String manufacturerId);
}