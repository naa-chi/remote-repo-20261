package com.gTransitProject.train.service;

import com.gTransitProject.train.model.typeTrain;
import com.gTransitProject.train.repo.typeTrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class typeTrainService {

    @Autowired
    private typeTrainRepository typeTrainRepository;

    public typeTrain createTypeTrain(typeTrain typeTrainData) {
        return typeTrainRepository.save(typeTrainData);
    }

    public List<typeTrain> getAllTypeTrains() {
        return typeTrainRepository.findAll();
    }

    public typeTrain getTypeTrainByID(Integer id) {
        return typeTrainRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid type"));
    }

    /*
        There is no update or delete method 
        because it makes no sense to be able to update or delete that.

    */
}
