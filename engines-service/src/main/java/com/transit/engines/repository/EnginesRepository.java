package com.transit.engines.repository;

import com.transit.engines.model.EngineModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnginesRepository extends JpaRepository<EngineModel, Long> {
    List<EngineModel> findByManufacturerId(String manufacturerId);
    List<EngineModel> findByEngineHorsepower(Float engineHorsepower);
    List<EngineModel> findByEngineCode(String engineCode);
    List<EngineModel> findByEngineWeight(Float engineWeight);
    List<EngineModel> findByEnginePrice(Float enginePrice);
}