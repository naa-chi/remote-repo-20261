package com.gTransitProject.station.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.station.model.station;
import com.gTransitProject.station.service.ServiceStation;

import io.swagger.v3.oas.annotations.Operation;

import com.gTransitProject.station.client.LineClient;
import com.gTransitProject.station.client.CityClient;

import com.gTransitProject.station.dto.LineDTO;
import com.gTransitProject.station.dto.CityDTO;

@RestController
@RequestMapping("/api/stations")
public class ControllerStation {

    @Autowired
    private ServiceStation stationServ;

    @Autowired
    private LineClient lineClient;

    @Autowired
    private CityClient cityClient;

    @Operation(summary = "Get all stations", description = "Retrieves a list of all stations in the database.")
    @GetMapping
    public List<station> getStations(){
        return stationServ.getStations();
    }

    @Operation(summary = "Get a station by ID", description = "Retrieves a specific station based on its unique identifier.")
    @GetMapping("/{id}")//V
    public station getStationById(@PathVariable Integer id){
        return stationServ.getStationById(id);
    }

    @Operation(summary = "Create a new station", description = "Creates a new station record in the database with the provided data.")
    @PostMapping
    public station saveStation(@RequestBody station station){
        return stationServ.saveStation(station);
    }

    @Operation(summary = "Update a station", description = "Updates an existing station record in the database with the provided data.")
    @PutMapping("/{id}") //V
    public station updateStation(@PathVariable Integer id,
                                 @RequestBody station station){

        return stationServ.updateStation(id, station);
    }

    @Operation(summary = "Delete a station", description = "Deletes an existing station record from the database based on its unique identifier.")
    @DeleteMapping("/{id}")//V
    public void deleteStation(@PathVariable Integer id){
        stationServ.deleteStation(id);
    }

    @Operation(summary = "Test Line Client", description = "Tests the LineClient by retrieving a line based on its number.")
  @GetMapping("/test-line/{lineNumber}")
public LineDTO testLine( //Necessary?
        @PathVariable Integer lineNumber){

    return lineClient.getLineByNumber(lineNumber);
}
    @Operation(summary = "Test City Client", description = "Tests the CityClient by retrieving a city based on its code.")
    @GetMapping("/test-city/{code}")
    public CityDTO testCity( //Necessary?
            @PathVariable String code){

        return cityClient.getCityByCode(code);
    }
}