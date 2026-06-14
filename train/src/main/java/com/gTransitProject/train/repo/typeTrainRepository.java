package com.gTransitProject.train.repo;
// test TfdjfdP WHY IS THE TRANSITP ABOVE IN ITALICS WHAT SENSE DOES THAT MAKE

import com.gTransitProject.train.model.typeTrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// This 'extends' gives you save(), findAll(), findById() for free!
public interface typeTrainRepository extends JpaRepository<typeTrain, Integer> {
}