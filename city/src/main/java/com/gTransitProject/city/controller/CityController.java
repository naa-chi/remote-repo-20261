package com.gTransitProject.city.controller;

import com.gTransitProject.city.model.City;
import com.gTransitProject.city.service.CityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {

        return ResponseEntity.ok(
                cityService.getAllCities());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<City> getCityByCode(
            @PathVariable String code) {

        return ResponseEntity.ok(
                cityService.findByCityCode(code));
    }

    @PostMapping
    public ResponseEntity<City> createCity(
            @RequestBody City city) {

        City newCity = cityService.saveCity(city);

        return new ResponseEntity<>(
                newCity,
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(
            @PathVariable Integer id,
            @RequestBody City city) {

        return ResponseEntity.ok(
                cityService.updateCity(id, city));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCity(
            @PathVariable Integer id) {

        cityService.deleteCity(id);

        return ResponseEntity.ok(
                "Ciudad eliminada correctamente");
    }
}