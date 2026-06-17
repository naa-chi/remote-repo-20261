package com.gTransitProject.supervisor.controller;

import com.gTransitProject.supervisor.model.Supervisor;
import com.gTransitProject.supervisor.service.SupervisorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @GetMapping
    public ResponseEntity<List<Supervisor>> getAllSupervisors() {
        return ResponseEntity.ok(
                supervisorService.getAllSupervisors());
    }

    @PostMapping
    public ResponseEntity<Supervisor> createSupervisor(
            @RequestBody Supervisor supervisor) {

        return ResponseEntity.ok(
                supervisorService.saveSupervisor(supervisor));
    }
    @GetMapping("/validate/{code}")
public ResponseEntity<Boolean>
validateSupervisor(
        @PathVariable String code){

    Supervisor supervisor =
            supervisorService
                    .findByCode(code);

    return ResponseEntity.ok(
            supervisor.getAuthorized()
    );
    
}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupervisor(
        @PathVariable Integer id){

    supervisorService.deleteSupervisor(id);

    return ResponseEntity.ok(
            "Supervisor eliminado");
    }

    @GetMapping("/{id}")
public ResponseEntity<Supervisor> getSupervisorById(
        @PathVariable Integer id){

    return ResponseEntity.ok(
            supervisorService.getSupervisorById(id));
}
}