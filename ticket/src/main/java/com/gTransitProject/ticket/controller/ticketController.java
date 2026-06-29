package com.gTransitProject.ticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.ticket.model.ticketModel;
import com.gTransitProject.ticket.service.ticketService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/tickets")
public class ticketController {

    @Autowired
    private ticketService service;

    @Autowired
    private ticketAssembler assembler;

    @Operation(summary = "Grabs every record there is.", description = "Calls a method that returns all stored tickets in the db.")
    @GetMapping
    public CollectionModel<EntityModel<ticketModel>> getTickets() {
        return assembler.toCollectionModel(service.getAll());
    }

    @Operation(summary = "Fetches a ticket by its ID.", description = "Retrieves a specific ticket based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ticketModel>> getTicketById(@PathVariable Integer id) {
        ticketModel ticket = service.getById(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(ticket));
    }

    // functionally a duplicate. Do we deprecate it? – we keep it but return HATEOAS
    @Operation(summary = "Fetches a ticket by its code.", description = "Retrieves a specific ticket based on its unique code.")
    @GetMapping("/code/{code}")
    public ResponseEntity<EntityModel<ticketModel>> getTicketByCode(@PathVariable String code) {
        ticketModel ticket = service.getByCode(code);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(ticket));
    }

    @Operation(summary = "Fetches a ticket by its origin city code.", description = "Retrieves a specific ticket based on its origin city code.")
    @GetMapping("/origin/{cityCodeOrigin}")
    public ResponseEntity<EntityModel<ticketModel>> getTicketByCityCodeOrigin(@PathVariable String cityCodeOrigin) {
        ticketModel ticket = service.getCityCodeOrigin(cityCodeOrigin);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(ticket));
    }

    @Operation(summary = "Fetches a ticket by its destination city code.", description = "Retrieves a specific ticket based on its destination city code.")
    @GetMapping("/destination/{cityCodeDestination}")
    public ResponseEntity<EntityModel<ticketModel>> getTicketByCityCodeDestination(@PathVariable String cityCodeDestination) {
        ticketModel ticket = service.getCityCodeDestination(cityCodeDestination);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(ticket));
    }

    @Operation(summary = "Creates a new ticket entry.", description = "Creates a new ticket record in the database using the provided data.")
    @PostMapping
    public ResponseEntity<EntityModel<ticketModel>> saveTicket(@RequestBody ticketModel ticket) {
        ticketModel saved = service.create(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Deletes an entry.", description = "Deletes a ticket record from the database based on the provided ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates an existing entry.", description = "Updates an existing ticket record in the database with the provided data.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ticketModel>> updateTicket(@PathVariable Integer id, @RequestBody ticketModel ticket) {
        ticketModel updated = service.update(id, ticket);
        return ResponseEntity.ok(assembler.toModel(updated));
    }
}