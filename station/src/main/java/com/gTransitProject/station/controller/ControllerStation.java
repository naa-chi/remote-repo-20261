package com.gTransitProject.station.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.station.model.station;
import com.gTransitProject.station.service.ServiceStation;

import io.swagger.v3.oas.annotations.Operation;

import com.gTransitProject.station.client.LineClient;
import com.gTransitProject.station.client.CityClient;

import com.gTransitProject.station.dto.LineDTO;
import com.gTransitProject.station.dto.CityDTO;

@RestController
@RequestMapping("/api/station")
public class ControllerStation {

    @Autowired
    private ServiceStation stationServ;

    @Autowired
    private StationAssembler assembler;

    @Autowired
    private LineClient lineClient;

    @Autowired
    private CityClient cityClient;

    @Operation(summary = "Get all stations", description = "Retrieves a list of all stations in the database.")
    @GetMapping
    public CollectionModel<EntityModel<station>> getStations() {
        return assembler.toCollectionModel(stationServ.getStations());
    }

    @Operation(summary = "Get a station by ID", description = "Retrieves a specific station based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<station>> getStationById(@PathVariable Integer id) {
        station station = stationServ.getStationById(id);
        if (station == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(station));
    }

    @Operation(summary = "Create a new station", description = "Creates a new station record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<EntityModel<station>> saveStation(@RequestBody station station) {
        station saved = stationServ.saveStation(station);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Update a station", description = "Updates an existing station record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<station>> updateStation(@PathVariable Integer id,
                                                              @RequestBody station station) {
        station updated = stationServ.updateStation(id, station);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @Operation(summary = "Delete a station", description = "Deletes an existing station record from the database based on its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Integer id) {
        stationServ.deleteStation(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Test Line Client", description = "Tests the LineClient by retrieving a line based on its number.")
    @GetMapping("/test-line/{lineNumber}")
    public LineDTO testLine(@PathVariable Integer lineNumber) {
        return lineClient.getLineByNumber(lineNumber);
    }

    @Operation(summary = "Test City Client", description = "Tests the CityClient by retrieving a city based on its code.")
    @GetMapping("/test-city/{code}")
    public CityDTO testCity(@PathVariable String code) {
        return cityClient.getCityByCode(code);
    }
}