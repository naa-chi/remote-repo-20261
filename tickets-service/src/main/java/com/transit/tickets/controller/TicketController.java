package com.transit.tickets.controller;

import com.transit.tickets.dto.request.TicketRequestDTO;
import com.transit.tickets.dto.response.TicketResponseDTO;
import com.transit.tickets.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all tickets", description = "Returns all stored tickets in the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all tickets"),
        @ApiResponse(responseCode = "404", description = "No tickets found")
    })
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<TicketResponseDTO>> getAllTickets() {
        List<TicketResponseDTO> tickets = service.getAllTickets();
        tickets.forEach(this::addLinks);
        CollectionModel<TicketResponseDTO> model = CollectionModel.of(tickets);
        model.add(linkTo(methodOn(TicketController.class).getAllTickets()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Get ticket by ID", description = "Retrieves a specific ticket based on its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved ticket", content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@Parameter(description = "Ticket ID", example = "1") @PathVariable Long id) {
        TicketResponseDTO ticket = service.getTicketById(id);
        addLinks(ticket);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Get ticket by code", description = "Retrieves a specific ticket based on its unique code.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved ticket", content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<TicketResponseDTO> getTicketByCode(@Parameter(description = "Ticket code", example = "TICKET001") @PathVariable String code) {
        TicketResponseDTO ticket = service.getTicketByCode(code);
        addLinks(ticket);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Get tickets by origin city code", description = "Retrieves tickets based on origin city code.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tickets"),
        @ApiResponse(responseCode = "404", description = "No tickets found")
    })
    @GetMapping("/origin/{cityCodeOrigin}")
    public ResponseEntity<CollectionModel<TicketResponseDTO>> getTicketsByCityCodeOrigin(
            @Parameter(description = "Origin city code", example = "LON") @PathVariable String cityCodeOrigin) {
        List<TicketResponseDTO> tickets = service.getTicketsByCityCodeOrigin(cityCodeOrigin);
        tickets.forEach(this::addLinks);
        CollectionModel<TicketResponseDTO> model = CollectionModel.of(tickets);
        model.add(linkTo(methodOn(TicketController.class).getTicketsByCityCodeOrigin(cityCodeOrigin)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Get tickets by destination city code", description = "Retrieves tickets based on destination city code.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tickets"),
        @ApiResponse(responseCode = "404", description = "No tickets found")
    })
    @GetMapping("/destination/{cityCodeDestination}")
    public ResponseEntity<CollectionModel<TicketResponseDTO>> getTicketsByCityCodeDestination(
            @Parameter(description = "Destination city code", example = "PAR") @PathVariable String cityCodeDestination) {
        List<TicketResponseDTO> tickets = service.getTicketsByCityCodeDestination(cityCodeDestination);
        tickets.forEach(this::addLinks);
        CollectionModel<TicketResponseDTO> model = CollectionModel.of(tickets);
        model.add(linkTo(methodOn(TicketController.class).getTicketsByCityCodeDestination(cityCodeDestination)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Create a new ticket", description = "Creates a new ticket record.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket created successfully", content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody TicketRequestDTO request) {
        TicketResponseDTO saved = service.createTicket(request);
        addLinks(saved);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Update an existing ticket", description = "Updates an existing ticket record.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket updated successfully", content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @Parameter(description = "Ticket ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody TicketRequestDTO request) {
        TicketResponseDTO updated = service.updateTicket(id, request);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a ticket", description = "Deletes a ticket record based on ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ticket deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@Parameter(description = "Ticket ID", example = "1") @PathVariable Long id) {
        service.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(TicketResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(TicketController.class).getTicketById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(TicketController.class).getAllTickets()).withRel("all-tickets"));
        }
    }
}