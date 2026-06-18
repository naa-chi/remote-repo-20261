package com.gTransitProject.typeengine.repo;

import com.gTransitProject.typeengine.model.typeEngine;

//import java.util.List;
//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface typeEngineRepo extends JpaRepository<typeEngine, Integer> {

}
