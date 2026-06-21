package com.gTransitProject.ticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.ticket.model.ticketModel;
import com.gTransitProject.ticket.service.ticketService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/tickets")
public class ticketController {
    @Autowired
    private ticketService service;

    @Operation(summary = "Grabs every record there is.", description = "Calls a method that returns all stored tickets in the db. ")
    @GetMapping
    public List<ticketModel> getTickets(){
        return service.getAll();
    }

    @Operation(summary = "Fetches a ticket by its ID.", description = "Retrieves a specific ticket based on its unique identifier.")
    @GetMapping("/{id}")
    public ticketModel getTicketById(@PathVariable Integer id){
        return service.getById(id);
    }

    @Operation(summary = "Fetches a ticket by its code.", description = "Retrieves a specific ticket based on its unique code.")
    @GetMapping("/code/{code}")
    public ticketModel getTicketByCode(@PathVariable String code){
        return service.getByCode(code);
    }

    @Operation(summary = "Fetches a ticket by its origin city code.", description = "Retrieves a specific ticket based on its origin city code.")
    @GetMapping("/origin/{cityCodeOrigin}")
    public ticketModel getTicketByCityCodeOrigin(@PathVariable String cityCodeOrigin){
        return service.getCityCodeOrigin(cityCodeOrigin);
    }

    @Operation(summary = "Fetches a ticket by its destination city code.", description = "Retrieves a specific ticket based on its destination city code.")
    @GetMapping("/destination/{cityCodeDestination}")
    public ticketModel getTicketByCityCodeDestination(@PathVariable String cityCodeDestination){
        return service.getCityCodeDestination(cityCodeDestination);
    }

    @Operation(summary = "Creates a new ticket entry.", description = "Creates a new ticket record in the database using the provided data.")
    @PostMapping
    public ticketModel saveTicket(@RequestBody ticketModel ticket){
        return service.create(ticket);
    }

    @Operation(summary = "Deletes an entry.", description = "Deletes a ticket record from the database based on the provided ID.")
    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Integer id){
        service.delete(id);
    }

    @Operation(summary = "Updates an existing entry.", description = "Updates an existing ticket record in the database with the provided data.")
    @PutMapping("/{id}")
    public ticketModel updateTicket(@PathVariable Integer id, @RequestBody ticketModel ticket){
        return service.update(id, ticket);
    }
}
