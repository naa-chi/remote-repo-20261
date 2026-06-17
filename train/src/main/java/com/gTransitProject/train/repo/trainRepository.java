package com.gTransitProject.train.repo;

import com.gTransitProject.train.model.train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// This 'extends' gives you save(), findAll(), findById() for free!
public interface trainRepository extends JpaRepository<train, Integer> {
    //implement update method here too SOMEWHERE
    
}