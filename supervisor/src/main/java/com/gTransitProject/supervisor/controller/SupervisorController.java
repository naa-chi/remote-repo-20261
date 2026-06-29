package com.gTransitProject.supervisor.controller;

import com.gTransitProject.supervisor.model.Supervisor;
import com.gTransitProject.supervisor.service.SupervisorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/supervisors")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private SupervisorAssembler assembler;

    @Operation(summary = "Get all supervisors", description = "Retrieves a list of all supervisors in the database.")
    @GetMapping
    public CollectionModel<EntityModel<Supervisor>> getAllSupervisors() {
        return assembler.toCollectionModel(supervisorService.getAllSupervisors());
    }

    @Operation(summary = "Create a new supervisor", description = "Creates a new supervisor record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<EntityModel<Supervisor>> createSupervisor(@RequestBody Supervisor supervisor) {
        Supervisor saved = supervisorService.saveSupervisor(supervisor);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Validate a supervisor", description = "Validates a supervisor based on their unique code and returns their authorization status.")
    @GetMapping("/validate/{code}")
    public ResponseEntity<EntityModel<Boolean>> validateSupervisor(@PathVariable String code) {
        Supervisor supervisor = supervisorService.findByCode(code);
        if (supervisor == null) {
            return ResponseEntity.notFound().build();
        }
        // Wrap the boolean in an EntityModel with a self link to the validation endpoint itself.
        EntityModel<Boolean> model = EntityModel.of(supervisor.getAuthorized(),
                linkTo(methodOn(SupervisorController.class).validateSupervisor(code)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Delete a supervisor", description = "Deletes a supervisor record from the database based on the provided ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupervisor(@PathVariable Integer id) {
        supervisorService.deleteSupervisor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a supervisor by ID", description = "Retrieves a specific supervisor based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Supervisor>> getSupervisorById(@PathVariable Integer id) {
        Supervisor supervisor = supervisorService.getSupervisorById(id);
        if (supervisor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(supervisor));
    }

    @Operation(summary = "Update a supervisor", description = "Updates an existing supervisor record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Supervisor>> updateSupervisor(@PathVariable Integer id,
                                                                    @RequestBody Supervisor supervisor) {
        Supervisor updated = supervisorService.updateSupervisor(id, supervisor);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }
}