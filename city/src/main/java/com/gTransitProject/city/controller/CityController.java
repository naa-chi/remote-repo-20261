package com.gTransitProject.city.controller;

import com.gTransitProject.city.model.City;
import com.gTransitProject.city.service.CityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

    @Autowired
    private CityAssembler assembler;

    @Operation(summary = "Get all cities", description = "Retrieves a list of all cities in the database.")
    @GetMapping
    public CollectionModel<EntityModel<City>> getAllCities() {
        return assembler.toCollectionModel(cityService.getAllCities());
    }

    @Operation(summary = "Get a city by code", description = "Retrieves a specific city based on its unique code.")
    @GetMapping("/code/{code}")
    public ResponseEntity<EntityModel<City>> getCityByCode(@PathVariable String code) {
        City city = cityService.findByCityCode(code);
        if (city == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(city));
    }

    @Operation(summary = "Get a city by ID", description = "Retrieves a specific city based on its unique identifier.")
    @PostMapping
    public ResponseEntity<EntityModel<City>> createCity(@Valid @RequestBody City city) {
        City saved = cityService.saveCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Update a city", description = "Updates an existing city record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<City>> updateCity(@PathVariable Integer id,
                                                        @Valid @RequestBody City city) {
        City updated = cityService.updateCity(id, city);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @Operation(summary = "Delete a city", description = "Throws an intercontinental ballistic missile at the city. That's the only reasonable option this endpoint could signify.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}