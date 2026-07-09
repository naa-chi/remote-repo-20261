package com.transit.cities.repository;

import com.transit.cities.model.CityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityModel, Long> {
    Optional<CityModel> findByThreeLetterCityCode(String cityCode);
}