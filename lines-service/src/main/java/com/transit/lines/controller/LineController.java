package com.transit.lines.controller;

import com.transit.lines.dto.request.LineRequestDTO;
import com.transit.lines.dto.response.LineResponseDTO;
import com.transit.lines.service.LineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/lines")
@Tag(name = "Lines", description = "Operations on railway lines")
public class LineController {

    private final LineService service;

    public LineController(LineService service) {
        this.service = service;
    }

    @Operation(summary = "Get all lines")
    @GetMapping
    public ResponseEntity<CollectionModel<LineResponseDTO>> getAll() {
        List<LineResponseDTO> lines = service.getAllLines();
        lines.forEach(this::addLinks);

        CollectionModel<LineResponseDTO> collection = CollectionModel.of(lines);
        collection.add(linkTo(methodOn(LineController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Get line by ID")
    @GetMapping("/{id}")
    public ResponseEntity<LineResponseDTO> getById(@PathVariable Long id) {
        LineResponseDTO dto = service.getLineById(id);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get line by numeric code")
    @GetMapping("/code/{lineCode}")
    public ResponseEntity<LineResponseDTO> getByCode(@PathVariable Integer lineCode) {
        LineResponseDTO dto = service.getLineByCode(lineCode);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new line")
    @PostMapping
    public ResponseEntity<LineResponseDTO> create(@Valid @RequestBody LineRequestDTO request) {
        LineResponseDTO created = service.createLine(request);
        addLinks(created);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update an existing line")
    @PutMapping("/{id}")
    public ResponseEntity<LineResponseDTO> update(@PathVariable Long id, @Valid @RequestBody LineRequestDTO request) {
        LineResponseDTO updated = service.updateLine(id, request);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a line")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(LineResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(LineController.class).getById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(LineController.class).getAll()).withRel("all-lines"));
        }
    }
}