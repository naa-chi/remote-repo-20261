package com.transit.trains.controller;

import com.transit.trains.dto.request.TrainRequestDTO;
import com.transit.trains.dto.response.TrainResponseDTO;
import com.transit.trains.service.TrainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/trains")
@Tag(name = "Trains", description = "Operations pertaining to fleet trains management")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @Operation(summary = "Get train by ID", description = "Fetches a train by its unique ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved train", content = @Content(schema = @Schema(implementation = TrainResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Train not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TrainResponseDTO> getTrain(@Parameter(description = "Train ID", example = "1") @PathVariable Long id) {
        TrainResponseDTO train = trainService.getTrainById(id);
        train.add(linkTo(methodOn(TrainController.class).getTrain(id)).withSelfRel());
        train.add(linkTo(methodOn(TrainController.class).getAllTrains()).withRel("all-trains"));
        return ResponseEntity.ok(train);
    }

    @Operation(summary = "Get train by code", description = "Fetches a train by its unique code.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved train", content = @Content(schema = @Schema(implementation = TrainResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Train not found")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<TrainResponseDTO> getTrainByCode(@Parameter(description = "Train code", example = "TRN01") @PathVariable String code) {
        TrainResponseDTO train = trainService.getTrainByCode(code);
        train.add(linkTo(methodOn(TrainController.class).getTrainByCode(code)).withSelfRel());
        train.add(linkTo(methodOn(TrainController.class).getTrain(train.getId())).withRel("train-details"));
        train.add(linkTo(methodOn(TrainController.class).getAllTrains()).withRel("all-trains"));
        return ResponseEntity.ok(train);
    }

    @Operation(summary = "Get trains by manufacturer ID", description = "Fetches trains by manufacturer ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of trains"),
        @ApiResponse(responseCode = "404", description = "No trains found")
    })
    @GetMapping("/manufacturer/{manufacturerId}")
    public ResponseEntity<CollectionModel<TrainResponseDTO>> getTrainsByManufacturerId(
            @Parameter(description = "Manufacturer ID", example = "Siemens") @PathVariable String manufacturerId) {
        List<TrainResponseDTO> trains = trainService.getTrainsByManufacturerId(manufacturerId);
        trains.forEach(train -> train.add(linkTo(methodOn(TrainController.class).getTrain(train.getId())).withRel("train-details")));
        
        CollectionModel<TrainResponseDTO> collectionModel = CollectionModel.of(trains);
        collectionModel.add(linkTo(methodOn(TrainController.class).getTrainsByManufacturerId(manufacturerId)).withSelfRel());
        collectionModel.add(linkTo(methodOn(TrainController.class).getAllTrains()).withRel("all-trains"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get all trains", description = "Fetches all trains in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all trains"),
        @ApiResponse(responseCode = "404", description = "No trains found")
    })
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<TrainResponseDTO>> getAllTrains() {
        List<TrainResponseDTO> trains = trainService.getAllTrains();
        trains.forEach(train -> train.add(linkTo(methodOn(TrainController.class).getTrain(train.getId())).withSelfRel()));
        
        CollectionModel<TrainResponseDTO> collectionModel = CollectionModel.of(trains);
        collectionModel.add(linkTo(methodOn(TrainController.class).getAllTrains()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get trains by engine ID", description = "Fetches all trains that use a specific engine.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved trains for engine"),
        @ApiResponse(responseCode = "404", description = "No trains found for this engine")
    })
    @GetMapping("/engine/{engineId}")
    public ResponseEntity<CollectionModel<TrainResponseDTO>> getTrainsByEngineId(
            @Parameter(description = "Engine ID", example = "1") @PathVariable Long engineId) {
        List<TrainResponseDTO> trains = trainService.getTrainsByEngineId(engineId);
        trains.forEach(train -> train.add(linkTo(methodOn(TrainController.class).getTrain(train.getId())).withSelfRel()));
        
        CollectionModel<TrainResponseDTO> collectionModel = CollectionModel.of(trains);
        collectionModel.add(linkTo(methodOn(TrainController.class).getTrainsByEngineId(engineId)).withSelfRel());
        collectionModel.add(linkTo(methodOn(TrainController.class).getAllTrains()).withRel("all-trains"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Create a new train", description = "Creates a new train in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Train created successfully", content = @Content(schema = @Schema(implementation = TrainResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/create")
    public ResponseEntity<TrainResponseDTO> createTrain(@Valid @RequestBody TrainRequestDTO trainDTO) {
        TrainResponseDTO savedTrain = trainService.createTrain(trainDTO);
        savedTrain.add(linkTo(methodOn(TrainController.class).getTrain(savedTrain.getId())).withSelfRel());
        savedTrain.add(linkTo(methodOn(TrainController.class).getAllTrains()).withRel("all-trains"));
        return ResponseEntity.ok(savedTrain);
    }

    @Operation(summary = "Delete train by ID", description = "Deletes a train by its unique ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Train deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Train not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@Parameter(description = "Train ID", example = "1") @PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }
}