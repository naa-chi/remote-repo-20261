package com.gTransitProject.city.repo;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gTransitProject.city.model.City;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    Optional<City> findByCityCode(String cityCode);

    Optional<City> findByCityName(String cityName);

}