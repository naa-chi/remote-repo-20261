package com.gTransitProject.supervisor.controller;

import com.gTransitProject.supervisor.model.Supervisor;
import com.gTransitProject.supervisor.service.SupervisorService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
        Why do we have this? Why isn't it organized properly? Why is the indentation
        all over the place? 
        todo: fix
*/

@RestController
@RequestMapping("/api/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @Operation(summary = "Get all supervisors", description = "Retrieves a list of all supervisors in the database.")
    @GetMapping
    public ResponseEntity<List<Supervisor>> getAllSupervisors() {
        return ResponseEntity.ok(supervisorService.getAllSupervisors());
    }

    @Operation(summary = "Create a new supervisor", description = "Creates a new supervisor record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<Supervisor> createSupervisor(@RequestBody Supervisor supervisor) {
        return ResponseEntity.ok(supervisorService.saveSupervisor(supervisor));
    }

        @Operation(summary = "Validate a supervisor", description = "Validates a supervisor based on their unique code and returns their authorization status.")
@GetMapping("/validate/{code}") //What does it mean to be? 
        public ResponseEntity<Boolean>
validateSupervisor(@PathVariable String code){

    Supervisor supervisor = supervisorService.findByCode(code);
    return ResponseEntity.ok(supervisor.getAuthorized()
    );
    
}

@Operation(summary = "Delete a supervisor", description = "Deletes a supervisor record from the database based on the provided ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupervisor(
        @PathVariable Integer id){

    supervisorService.deleteSupervisor(id);

    return ResponseEntity.ok(
            "Supervisor eliminado");
    }
        @Operation(summary = "Get a supervisor by ID", description = "Retrieves a specific supervisor based on its unique identifier.")
        @GetMapping("/{id}")
        public ResponseEntity<Supervisor> getSupervisorById(
                @PathVariable Integer id){

        return ResponseEntity.ok(
            supervisorService.getSupervisorById(id));
        }

        @Operation(summary = "Update a supervisor", description = "Updates an existing supervisor record in the database with the provided data.")
        @PutMapping("/{id}")
        public ResponseEntity<Supervisor>
        updateSupervisor(
                @PathVariable Integer id,
                @RequestBody Supervisor supervisor){

        return ResponseEntity.ok(
                supervisorService
                        .updateSupervisor(
                                id,
                                supervisor));
        }

}