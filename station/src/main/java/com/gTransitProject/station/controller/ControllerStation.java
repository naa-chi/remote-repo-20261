package com.gTransitProject.station.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.station.model.station;
import com.gTransitProject.station.service.ServiceStation;

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

    @GetMapping
    public List<station> getStations(){
        return stationServ.getStations();
    }

    @GetMapping("/{id}")
    public station getStationById(@PathVariable Integer id){
        return stationServ.getStationById(id);
    }

    @PostMapping
    public station saveStation(@RequestBody station station){
        return stationServ.saveStation(station);
    }

    @PutMapping("/{id}")
    public station updateStation(@PathVariable Integer id,
                                 @RequestBody station station){

        return stationServ.updateStation(id, station);
    }

    @DeleteMapping("/{id}")
    public void deleteStation(@PathVariable Integer id){
        stationServ.deleteStation(id);
    }

  @GetMapping("/test-line/{lineNumber}")
public LineDTO testLine(
        @PathVariable Integer lineNumber){

    return lineClient.getLineByNumber(lineNumber);
}
    @GetMapping("/test-city/{code}")
    public CityDTO testCity(
            @PathVariable String code){

        return cityClient.getCityByCode(code);
    }
}