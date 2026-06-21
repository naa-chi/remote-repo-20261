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

    public train getTrainByCode(String code) {
        return trainRepository.findAll().stream()
                .filter(train -> train.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found with code: " + code));
    }

    public train getByManufacturerId(Integer manufacturerId) {
        return trainRepository.findAll().stream()
                .filter(train -> train.getManufacturerId().equals(manufacturerId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found with manufacturer ID: " + manufacturerId));
    }

    public train getByTypeTrain(Integer typeTrainId) {
        return trainRepository.findAll().stream()
                .filter(train -> train.getTypeTrain().getId().equals(typeTrainId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found with type ID: " + typeTrainId));
    }

    public void deleteTrain(Integer id) {
        if (!trainRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found");
        }
        trainRepository.deleteById(id);
    }

    public train updateTrain(Integer id, train updatedTrain) {
        return trainRepository.findById(id)
                .map(train -> {
                    train.setCode(updatedTrain.getCode());
                    train.setTypeTrain(updatedTrain.getTypeTrain());
                    train.setManufacturerId(updatedTrain.getManufacturerId());
                    return trainRepository.save(train);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found"));
    }
}