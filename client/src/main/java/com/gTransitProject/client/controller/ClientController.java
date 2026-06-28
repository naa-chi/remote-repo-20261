package com.gTransitProject.client.controller;

import com.gTransitProject.client.model.client;
import com.gTransitProject.client.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients in the database.")
    @GetMapping
    public ResponseEntity<List<client>>
    getAllClients(){

        return ResponseEntity.ok(
                clientService.getAllClients());
    }

    @Operation(summary = "Create a new client", description = "Creates a new client record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<client>
    createClient(
            @RequestBody client clientb){

        return ResponseEntity.ok(
                clientService.saveClient(clientb));
    }
    @Operation(summary = "Get a client by ID", description = "Retrieves a specific client based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<client>
    getClientById(
            @PathVariable Integer id){

        return ResponseEntity.ok(
                clientService.getClientById(id));
    }
        @Operation(summary = "Update a client", description = "Updates an existing client record in the database with the provided data.")
        @PutMapping("/{id}")
        public ResponseEntity<client>
        updateClient(
                @PathVariable Integer id,
                @RequestBody client clientb){

        return ResponseEntity.ok(
                clientService.updateClient(
                        id,
                        clientb));
}
        @Operation(summary = "Delete a client", description = "Deletes an existing client record from the database based on its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteClient(
            @PathVariable Integer id){

        clientService.deleteClient(id);

        return ResponseEntity.ok(
                "Cliente eliminado");
    }
    @Operation(summary = "Validate a client", description = "Validates a client based on their unique code and returns their status.")
@GetMapping("/validate/{code}") //Why do we have a specific code block for user validation?
public ResponseEntity<String>
validateClient(
        @PathVariable String code){

    client clientb =
            clientService
                    .findByAuthCode(code);

    return ResponseEntity.ok(
            clientb.getStatus());
}
}