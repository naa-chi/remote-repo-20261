package com.transit.tickets.controller;

import com.transit.tickets.dto.request.TicketRequestDTO;
import com.transit.tickets.dto.response.TicketResponseDTO;
import com.transit.tickets.service.TicketService;
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
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Operations pertaining to transit tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @Operation(summary = "Grabs every record there is.", description = "Returns all stored tickets in the db.")
    @GetMapping
    public ResponseEntity<CollectionModel<TicketResponseDTO>> getTickets() {
        List<TicketResponseDTO> tickets = service.getAllTickets();
        tickets.forEach(this::addLinks);
        CollectionModel<TicketResponseDTO> model = CollectionModel.of(tickets);
        model.add(linkTo(methodOn(TicketController.class).getTickets()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Fetches a ticket by its ID.", description = "Retrieves a specific ticket based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        TicketResponseDTO ticket = service.getTicketById(id);
        addLinks(ticket);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Fetches a ticket by its code.", description = "Retrieves a specific ticket based on its unique code.")
    @GetMapping("/code/{code}")
    public ResponseEntity<TicketResponseDTO> getTicketByCode(@PathVariable String code) {
        TicketResponseDTO ticket = service.getTicketByCode(code);
        addLinks(ticket);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Fetches tickets by origin city code.", description = "Retrieves tickets based on origin city code.")
    @GetMapping("/origin/{cityCodeOrigin}")
    public ResponseEntity<CollectionModel<TicketResponseDTO>> getTicketsByCityCodeOrigin(@PathVariable String cityCodeOrigin) {
        List<TicketResponseDTO> tickets = service.getTicketsByCityCodeOrigin(cityCodeOrigin);
        tickets.forEach(this::addLinks);
        CollectionModel<TicketResponseDTO> model = CollectionModel.of(tickets);
        model.add(linkTo(methodOn(TicketController.class).getTicketsByCityCodeOrigin(cityCodeOrigin)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Fetches tickets by destination city code.", description = "Retrieves tickets based on destination city code.")
    @GetMapping("/destination/{cityCodeDestination}")
    public ResponseEntity<CollectionModel<TicketResponseDTO>> getTicketsByCityCodeDestination(@PathVariable String cityCodeDestination) {
        List<TicketResponseDTO> tickets = service.getTicketsByCityCodeDestination(cityCodeDestination);
        tickets.forEach(this::addLinks);
        CollectionModel<TicketResponseDTO> model = CollectionModel.of(tickets);
        model.add(linkTo(methodOn(TicketController.class).getTicketsByCityCodeDestination(cityCodeDestination)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Creates a new ticket entry.", description = "Creates a new ticket record.")
    @PostMapping
    public ResponseEntity<TicketResponseDTO> saveTicket(@Valid @RequestBody TicketRequestDTO request) {
        TicketResponseDTO saved = service.createTicket(request);
        addLinks(saved);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Updates an existing entry.", description = "Updates an existing ticket record.")
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketRequestDTO request) {
        TicketResponseDTO updated = service.updateTicket(id, request);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletes an entry.", description = "Deletes a ticket record based on ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        service.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(TicketResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(TicketController.class).getTicketById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(TicketController.class).getTickets()).withRel("all-tickets"));
        }
    }
}