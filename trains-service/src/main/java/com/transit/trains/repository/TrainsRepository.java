package com.transit.trains.repository;

import com.transit.trains.model.TrainModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainsRepository extends JpaRepository<TrainModel, Long> {

    List<TrainModel> findByManufacturerId(String manufacturerId);

    Optional<TrainModel> findByCode(String code);

    List<TrainModel> findByEngineId(Long engineId);
}