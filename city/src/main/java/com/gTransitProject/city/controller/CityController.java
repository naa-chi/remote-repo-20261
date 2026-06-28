package com.gTransitProject.city.controller;

import com.gTransitProject.city.model.City;
import com.gTransitProject.city.service.CityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @Operation(summary = "Get all cities", description = "Retrieves a list of all cities in the database.")
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {

        return ResponseEntity.ok(
                cityService.getAllCities());
    }

        @Operation(summary = "Get a city by code", description = "Retrieves a specific city based on its unique code.")
    @GetMapping("/code/{code}")
    public ResponseEntity<City> getCityByCode(
            @PathVariable String code) {

        return ResponseEntity.ok(
                cityService.findByCityCode(code));
    }

    @Operation(summary = "Get a city by ID", description = "Retrieves a specific city based on its unique identifier.")
    @PostMapping
public ResponseEntity<City> createCity(
        @Valid @RequestBody City city) {

    City newCity = cityService.saveCity(city);

    return new ResponseEntity<>(
            newCity,
            HttpStatus.CREATED);
}

    @Operation(summary = "Update a city", description = "Updates an existing city record in the database with the provided data.")
    @PutMapping("/{id}")
public ResponseEntity<City> updateCity(
        @PathVariable Integer id,
        @Valid @RequestBody City city) {

    return ResponseEntity.ok(
            cityService.updateCity(id, city));
}

        @Operation(summary = "Delete a city", description = "Throws an intercontinental ballistic missile at the city. That's the only reasonable option this endpoint could signify.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCity(
            @PathVariable Integer id) {

        cityService.deleteCity(id);

        return ResponseEntity.ok(
                "Ciudad eliminada correctamente");
    }
}