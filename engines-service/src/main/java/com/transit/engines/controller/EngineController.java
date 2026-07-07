package com.transit.engines.controller;

import com.transit.engines.dto.EngineDTO;
import com.transit.engines.model.EngineModel;
import com.transit.engines.service.EngineService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/engines")
public class EngineController {

    private final EngineService engineService;

    public EngineController(EngineService engineService) {
        this.engineService = engineService;
    }

    // ==========================================
    // SINGLE ITEM ENDPOINT
    // ==========================================

    @Operation(summary = "Get an engine by ID")
    @GetMapping("/{id}")
    public ResponseEntity<EngineDTO> getEngineById(@PathVariable Long id) {
        EngineDTO engine = engineService.getEngineById(id);
        
        // Single item links
        engine.add(linkTo(methodOn(EngineController.class).getEngineById(id)).withSelfRel());
        engine.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));
        
        return ResponseEntity.ok(engine);
    }

    // ==========================================
    // LIST ENDPOINTS
    // ==========================================

    @Operation(summary = "Get a list of engines by manufacturer")
    @GetMapping("/manufacturer/{code}")
    public ResponseEntity<CollectionModel<EngineDTO>> getEnginesByManufacturerCode(@PathVariable String code) {
        List<EngineDTO> engines = engineService.getEnginesByManufacturerId(code);
        engines.forEach(this::addItemLinks); // Apply self-links to individual engines

        CollectionModel<EngineDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByManufacturerCode(code)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get a list of engines by engine code")
    @GetMapping("/engineCode/{engineCode}")
    public ResponseEntity<CollectionModel<EngineDTO>> getEnginesByEngineCode(@PathVariable String engineCode) {
        List<EngineDTO> engines = engineService.getEnginesByEngineCode(engineCode);
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByEngineCode(engineCode)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get a list of engines by horsepower")
    @GetMapping("/horsepower/{horsepower}")
    public ResponseEntity<CollectionModel<EngineDTO>> getEnginesByHorsepower(@PathVariable Float horsepower) {
        List<EngineDTO> engines = engineService.getEngineHorsepower(horsepower);
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByHorsepower(horsepower)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get a list of engines by price")
    @GetMapping("/price/{price}")
    public ResponseEntity<CollectionModel<EngineDTO>> getEnginesByPrice(@PathVariable Float price) {
        List<EngineDTO> engines = engineService.getEnginePrice(price);
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByPrice(price)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get a list of engines by weight")
    @GetMapping("/weight/{weight}")
    public ResponseEntity<CollectionModel<EngineDTO>> getEnginesByWeight(@PathVariable Float weight) {
        List<EngineDTO> engines = engineService.getEngineWeight(weight);
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByWeight(weight)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get all engines")
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EngineDTO>> getAllEngines() {
        List<EngineDTO> engines = engineService.getAllEngines();
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping("/create")
    public ResponseEntity<EngineDTO> createEngine(@Valid @RequestBody EngineDTO engineDTO) {
        EngineDTO created = engineService.createEngine(engineDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EngineDTO> updateEngine(@PathVariable Long id, @Valid @RequestBody EngineDTO engineDTO) {
        return ResponseEntity.ok(engineService.updateEngine(id, engineDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEngine(@PathVariable Long id) {
        engineService.deleteEngine(id);
        return ResponseEntity.noContent().build();
    }
    

    // ==========================================
    // HELPER METHODS
    // ==========================================

    /**
     * Adds a "self" link to an individual EngineDTO.
     * Used when iterating over a List<EngineDTO> to build a CollectionModel.
     */
    private void addItemLinks(EngineDTO engine) {
        // Only add the link if the engine has a valid ID (e.g., not a -1L fallback error DTO)
        if (engine.getId() != null && engine.getId() > 0) {
            engine.add(linkTo(methodOn(EngineController.class).getEngineById(engine.getId())).withSelfRel());
        }
    }
}