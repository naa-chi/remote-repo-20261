package com.gTransitProject.train.service;

import com.gTransitProject.train.model.train;
import com.gTransitProject.train.repo.trainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class trainService {

    @Autowired
    private trainRepository trainRepository;

    public train createTrain(train trainData) {
        // These methods exist now because of the @Data in the train model
        return trainRepository.save(trainData);
    }

    public List<train> getAllTrains() {
        return trainRepository.findAll();
    }

    public train getTrainById(Integer id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found"));
    }
}