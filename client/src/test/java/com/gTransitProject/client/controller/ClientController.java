package com.gTransitProject.client.controller;

import com.gTransitProject.client.model.client;
import com.gTransitProject.client.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientAssembler assembler;

    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients in the database.")
    @GetMapping
    public CollectionModel<EntityModel<client>> getAllClients() {
        return assembler.toCollectionModel(clientService.getAllClients());
    }

    @Operation(summary = "Create a new client", description = "Creates a new client record in the database with the provided data.")
    @PostMapping
    public ResponseEntity<EntityModel<client>> createClient(@RequestBody client clientb) {
        client saved = clientService.saveClient(clientb);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Get a client by ID", description = "Retrieves a specific client based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<client>> getClientById(@PathVariable Integer id) {
        client clientb = clientService.getClientById(id);
        if (clientb == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(clientb));
    }

    @Operation(summary = "Update a client", description = "Updates an existing client record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<client>> updateClient(@PathVariable Integer id,
                                                            @RequestBody client clientb) {
        client updated = clientService.updateClient(id, clientb);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @Operation(summary = "Delete a client", description = "Deletes an existing client record from the database based on its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validate a client", description = "Validates a client based on their unique code and returns their status.")
    @GetMapping("/validate/{code}")
    public ResponseEntity<EntityModel<String>> validateClient(@PathVariable String code) {
        client clientb = clientService.findByAuthCode(code);
        if (clientb == null) {
            return ResponseEntity.notFound().build();
        }
        // Wrap the status string in an EntityModel with a self link to the validation endpoint
        EntityModel<String> model = EntityModel.of(clientb.getStatus(),
                linkTo(methodOn(ClientController.class).validateClient(code)).withSelfRel());
        return ResponseEntity.ok(model);
    }
}