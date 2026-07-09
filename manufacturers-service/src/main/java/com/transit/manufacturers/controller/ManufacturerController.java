package com.transit.manufacturers.controller;

import com.transit.manufacturers.dto.request.ManufacturerRequestDTO;
import com.transit.manufacturers.dto.response.ManufacturerResponseDTO;
import com.transit.manufacturers.service.ManufacturerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/manufacturers")
@Tag(name = "Manufacturers", description = "Operations pertaining to manufacturer management")
public class ManufacturerController {

    private final ManufacturerService service;

    public ManufacturerController(ManufacturerService service) {
        this.service = service;
    }

    @Operation(summary = "Get all manufacturers")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Successfully retrieved list"))
    @GetMapping
    public ResponseEntity<CollectionModel<ManufacturerResponseDTO>> getAll() {
        List<ManufacturerResponseDTO> manufacturers = service.getAllManufacturers();
        manufacturers.forEach(this::addLinks);

        CollectionModel<ManufacturerResponseDTO> collection = CollectionModel.of(manufacturers);
        collection.add(linkTo(methodOn(ManufacturerController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Get manufacturer by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerResponseDTO> getById(@PathVariable Long id) {
        ManufacturerResponseDTO dto = service.getManufacturerById(id);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get manufacturer by code")
    @GetMapping("/code/{manufacturerId}")
    public ResponseEntity<ManufacturerResponseDTO> getByCode(@PathVariable String manufacturerId) {
        ManufacturerResponseDTO dto = service.getManufacturerByCode(manufacturerId);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new manufacturer")
    @PostMapping
    public ResponseEntity<ManufacturerResponseDTO> create(@Valid @RequestBody ManufacturerRequestDTO request) {
        ManufacturerResponseDTO created = service.createManufacturer(request);
        addLinks(created);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update an existing manufacturer")
    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerResponseDTO> update(@PathVariable Long id,
                                                           @Valid @RequestBody ManufacturerRequestDTO request) {
        ManufacturerResponseDTO updated = service.updateManufacturer(id, request);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a manufacturer")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteManufacturer(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(ManufacturerResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(ManufacturerController.class).getById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(ManufacturerController.class).getAll()).withRel("all-manufacturers"));
        }
    }
}