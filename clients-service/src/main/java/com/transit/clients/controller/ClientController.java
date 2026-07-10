package com.transit.clients.controller;

import com.transit.clients.dto.request.ClientRequestDTO;
import com.transit.clients.dto.response.ClientResponseDTO;
import com.transit.clients.service.ClientService;
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
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Client management")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @Operation(summary = "Get all clients")
    @GetMapping
    public ResponseEntity<CollectionModel<ClientResponseDTO>> getAll() {
        List<ClientResponseDTO> clients = service.getAllClients();
        clients.forEach(this::addLinks);
        CollectionModel<ClientResponseDTO> collection = CollectionModel.of(clients);
        collection.add(linkTo(methodOn(ClientController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Get client by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getById(@PathVariable Long id) {
        ClientResponseDTO dto = service.getClientById(id);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get client by code")
    @GetMapping("/code/{code}")
    public ResponseEntity<ClientResponseDTO> getByCode(@PathVariable String code) {
        ClientResponseDTO dto = service.getClientByCode(code);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new client")
    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO request) {
        ClientResponseDTO created = service.createClient(request);
        addLinks(created);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update an existing client")
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ClientRequestDTO request) {
        ClientResponseDTO updated = service.updateClient(id, request);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a client")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(ClientResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(ClientController.class).getById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(ClientController.class).getAll()).withRel("all-clients"));
        }
    }
}