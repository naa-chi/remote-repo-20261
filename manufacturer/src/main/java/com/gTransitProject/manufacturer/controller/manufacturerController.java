package com.gTransitProject.manufacturer.controller;

import com.gTransitProject.manufacturer.model.manufacturerModel;
import com.gTransitProject.manufacturer.service.manufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class manufacturerController {

    private final manufacturerService service;

    @GetMapping
    public List<manufacturerModel> findAll() {
        return service.getAll();
    }

    @GetMapping("/searchId/{id}")
    public ResponseEntity<manufacturerModel> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/searchName/{name}")
    public ResponseEntity<manufacturerModel> findByName(@PathVariable String name) {
        return ResponseEntity.ok(service.getByName(name));
    }

    @GetMapping("/searchCountry/{country}")
    public List<manufacturerModel> findByCountry(@PathVariable String country) {
        return service.getByCountry(country);
    }

    @PostMapping
    public manufacturerModel save(@RequestBody manufacturerModel model) {
        return service.create(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}