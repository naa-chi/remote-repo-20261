package com.gTransitProject.line.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.line.model.Line;
import com.gTransitProject.line.service.ServiceLine;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/lines")
public class ControllerLine {

    @Autowired
    private ServiceLine lineServ;

    @Autowired
    private LineAssembler assembler;

    @Operation(summary = "Get all lines", description = "Retrieves a list of all lines in the database.")
    @GetMapping
    public CollectionModel<EntityModel<Line>> getLines() {
        return assembler.toCollectionModel(lineServ.getLines());
    }

    @Operation(summary = "Get a line by ID", description = "Retrieves a specific line based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Line>> getLineById(@PathVariable Integer id) {
        Line line = lineServ.getLineById(id);
        if (line == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(line));
    }

    @Operation(summary = "Get a line by number", description = "Retrieves a specific line based on its unique number.")
    @GetMapping("/number/{lineNumber}")
    public ResponseEntity<EntityModel<Line>> getLineByNumber(@PathVariable Integer lineNumber) {
        Line line = lineServ.getLineByNumber(lineNumber);
        if (line == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(line));
    }

    @Operation(summary = "Create a new line", description = "Creates a new line record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<EntityModel<Line>> saveLine(@RequestBody Line line) {
        Line saved = lineServ.saveLine(line);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Update a line", description = "Updates an existing line record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Line>> updateLine(@PathVariable Integer id,
                                                        @RequestBody Line line) {
        Line updated = lineServ.updateLine(id, line);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @Operation(summary = "Delete a line", description = "Deletes an existing line record from the database based on its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Integer id) {
        lineServ.deleteLine(id);
        return ResponseEntity.noContent().build();
    }
}