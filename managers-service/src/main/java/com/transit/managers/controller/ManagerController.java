package com.transit.managers.controller;

import com.transit.managers.dto.request.ManagerRequestDTO;
import com.transit.managers.dto.response.ManagerResponseDTO;
import com.transit.managers.service.ManagerService;
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
@RequestMapping("/api/managers")
@Tag(name = "Managers", description = "Manager management")
public class ManagerController {

    private final ManagerService service;

    public ManagerController(ManagerService service) {
        this.service = service;
    }

    @Operation(summary = "Get all managers")
    @GetMapping
    public ResponseEntity<CollectionModel<ManagerResponseDTO>> getAll() {
        List<ManagerResponseDTO> managers = service.getAllManagers();
        managers.forEach(this::addLinks);
        CollectionModel<ManagerResponseDTO> collection = CollectionModel.of(managers);
        collection.add(linkTo(methodOn(ManagerController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Get manager by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> getById(@PathVariable Long id) {
        ManagerResponseDTO dto = service.getManagerById(id);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get manager by code")
    @GetMapping("/code/{code}")
    public ResponseEntity<ManagerResponseDTO> getByCode(@PathVariable String code) {
        ManagerResponseDTO dto = service.getManagerByCode(code);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new manager")
    @PostMapping
    public ResponseEntity<ManagerResponseDTO> create(@Valid @RequestBody ManagerRequestDTO request) {
        ManagerResponseDTO created = service.createManager(request);
        addLinks(created);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update an existing manager")
    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ManagerRequestDTO request) {
        ManagerResponseDTO updated = service.updateManager(id, request);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a manager")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteManager(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(ManagerResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(ManagerController.class).getById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(ManagerController.class).getAll()).withRel("all-managers"));
        }
    }
}