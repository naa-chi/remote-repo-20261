package com.transit.drivers.controller;

import com.transit.drivers.dto.request.DriverRequestDTO;
import com.transit.drivers.dto.response.DriverResponseDTO;
import com.transit.drivers.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/drivers")
@Tag(name = "Drivers", description = "Driver management")
public class DriverController {

    private final DriverService service;

    public DriverController(DriverService service) {
        this.service = service;
    }

    @Operation(summary = "Get all drivers")
    @GetMapping
    public ResponseEntity<CollectionModel<DriverResponseDTO>> getAll() {
        List<DriverResponseDTO> drivers = service.getAllDrivers();
        drivers.forEach(this::addLinks);
        CollectionModel<DriverResponseDTO> collection = CollectionModel.of(drivers);
        collection.add(linkTo(methodOn(DriverController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Get driver by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> getById(@PathVariable Long id) {
        DriverResponseDTO dto = service.getDriverById(id);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get driver by code")
    @GetMapping("/code/{code}")
    public ResponseEntity<DriverResponseDTO> getByCode(@PathVariable String code) {
        DriverResponseDTO dto = service.getDriverByCode(code);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new driver")
    @PostMapping
    public ResponseEntity<DriverResponseDTO> create(@Valid @RequestBody DriverRequestDTO request) {
        DriverResponseDTO created = service.createDriver(request);
        addLinks(created);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update an existing driver")
    @PutMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> update(@PathVariable Long id, @Valid @RequestBody DriverRequestDTO request) {
        DriverResponseDTO updated = service.updateDriver(id, request);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a driver")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(DriverResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(DriverController.class).getById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(DriverController.class).getAll()).withRel("all-drivers"));
        }
    }
}