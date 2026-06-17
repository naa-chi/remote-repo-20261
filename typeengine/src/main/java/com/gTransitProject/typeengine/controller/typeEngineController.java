package com.gTransitProject.typeengine.controller;

import com.gTransitProject.typeengine.model.typeEngine;
import com.gTransitProject.typeengine.service.typeEngineService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/engine")
public class typeEngineController {

    @Autowired
    private typeEngineService service;

    @PostMapping
    public ResponseEntity<typeEngine> createTypeEngine(@RequestBody typeEngine typeEngineData) {
        typeEngine savedTypeEngine = service.createTypeEngine(typeEngineData);
        return ResponseEntity.status(201).body(savedTypeEngine);
    }

    @GetMapping
    public List<typeEngine> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<typeEngine> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
