package com.gTransitProject.manufacturer.repo;

import com.gTransitProject.manufacturer.model.manufacturerModel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// This 'extends' gives you save(), findAll(), findById() for free!
public interface manufacturerRepo extends JpaRepository<manufacturerModel, Integer> {
    Optional<manufacturerModel> findByName(String name);

    // Returns a list since multiple manufacturers might be from the same country
    List<manufacturerModel> findByCountry(String country);
}