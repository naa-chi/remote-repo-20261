package com.transit.trains.controller;

import com.transit.trains.dto.TrainDTO;
import com.transit.trains.service.TrainService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

// Required for building HATEOAS links dynamically
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @Operation(summary = "Get train by ID", description = "Fetches a train by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<TrainDTO> getTrain(@PathVariable Long id) {
        TrainDTO train = trainService.getTrainById(id);
        
        // Add HATEOAS links
        train.add(linkTo(methodOn(TrainController.class).getTrain(id)).withSelfRel());
        train.add(linkTo(methodOn(TrainController.class).getAllTrains()).withRel("all-trains"));
        
        return ResponseEntity.ok(train);
    }

    @Operation(summary = "Get train by code", description = "Fetches a train by its unique code.")
    @GetMapping("/code/{code}")
    public ResponseEntity<TrainDTO> getTrainByCode(@PathVariable String code) {
        TrainDTO train = trainService.getTrainByCode(code);
        
        // Add HATEOAS links
        train.add(linkTo(methodOn(TrainController.class).getTrainByCode(code)).withSelfRel());
        train.add(linkTo(methodOn(TrainController.class).getTrain(train.getId())).withRel("train-details"));
        train.add(linkTo(methodOn(TrainController.class).getAllTrains()).withRel("all-trains"));
        
        return ResponseEntity.ok(train);
    }

    @Operation(summary = "Get train by manufacturer ID", description = "Fetches a train by its manufacturer ID.")
    @GetMapping("/manufacturer/{manufacturerId}")
    public ResponseEntity<CollectionModel<TrainDTO>> getTrainsByManufacturerId(@PathVariable String manufacturerId) {
        List<TrainDTO> trains = trainService.getTrainsByManufacturerId(manufacturerId);
        for (TrainDTO train : trains) {
            train.add(linkTo(methodOn(TrainController.class).getTrain(train.getId())).withRel("train-details"));
        }
        
        CollectionModel<TrainDTO> collectionModel = CollectionModel.of(trains);
        collectionModel.add(linkTo(methodOn(TrainController.class).getTrainsByManufacturerId(manufacturerId)).withSelfRel());
        collectionModel.add(linkTo(methodOn(TrainController.class).getAllTrains()).withRel("all-trains"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get all trains", description = "Fetches all trains in the system.")
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<TrainDTO>> getAllTrains() {
        List<TrainDTO> trains = trainService.getAllTrains();
        
        // Add self link to each individual train in the collection
        for (TrainDTO train : trains) {
            train.add(linkTo(methodOn(TrainController.class).getTrain(train.getId())).withSelfRel());
        }
        
        // Wrap the list in a CollectionModel and add a self link to the collection itself
        CollectionModel<TrainDTO> collectionModel = CollectionModel.of(trains);
        collectionModel.add(linkTo(methodOn(TrainController.class).getAllTrains()).withSelfRel());
        
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Delete train by ID", description = "Deletes a train by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new train", description = "Creates a new train in the system.")
    @PostMapping("/create")
    public ResponseEntity<TrainDTO> createTrain(@Valid @RequestBody TrainDTO trainDTO) {
        TrainDTO savedTrain = trainService.createTrain(trainDTO);
        
        // Add HATEOAS links to the newly created resource
        savedTrain.add(linkTo(methodOn(TrainController.class).getTrain(savedTrain.getId())).withSelfRel());
        savedTrain.add(linkTo(methodOn(TrainController.class).getAllTrains()).withRel("all-trains"));
        
        return ResponseEntity.ok(savedTrain);
    }

    @Operation(summary = "Trigger an error", description = "Endpoint to trigger a runtime error for testing purposes.")
    @GetMapping("/error")
    public ResponseEntity<String> triggerError() {
        throw new RuntimeException("You are seeing an error code because you did not add any further pathing. Please see docs.");
    }
}