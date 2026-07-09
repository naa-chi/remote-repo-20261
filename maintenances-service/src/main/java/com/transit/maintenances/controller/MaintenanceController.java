package com.transit.maintenances.controller;

import com.transit.maintenances.dto.request.MaintenanceRequestDTO;
import com.transit.maintenances.dto.response.MaintenanceResponseDTO;
import com.transit.maintenances.service.MaintenanceService;
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
@RequestMapping("/api/maintenances")
@Tag(name = "Maintenances", description = "Operations pertaining to maintenance reports management")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @Operation(summary = "Get all maintenance reports", description = "Returns all stored maintenance reports in the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all maintenance reports"),
        @ApiResponse(responseCode = "404", description = "No maintenance reports found")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<MaintenanceResponseDTO>> getAllMaintenances() {
        List<MaintenanceResponseDTO> maintenances = maintenanceService.getAllMaintenances();
        maintenances.forEach(this::addLinks);

        CollectionModel<MaintenanceResponseDTO> collectionModel = CollectionModel.of(maintenances);
        collectionModel.add(linkTo(methodOn(MaintenanceController.class).getAllMaintenances()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get maintenance by ID", description = "Retrieves a specific maintenance report based on its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved maintenance report", content = @Content(schema = @Schema(implementation = MaintenanceResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Maintenance report not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> getMaintenanceById(
            @Parameter(description = "Maintenance ID", example = "1") @PathVariable Long id) {
        MaintenanceResponseDTO maintenance = maintenanceService.getMaintenanceById(id);
        addLinks(maintenance);
        return ResponseEntity.ok(maintenance);
    }

    @Operation(summary = "Get maintenance by code", description = "Retrieves a specific maintenance report based on its unique maintenance code.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved maintenance report", content = @Content(schema = @Schema(implementation = MaintenanceResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Maintenance report not found")
    })
    @GetMapping("/code/{maintenanceId}")
    public ResponseEntity<MaintenanceResponseDTO> getMaintenanceByCode(
            @Parameter(description = "Maintenance code", example = "MNT-001") @PathVariable String maintenanceId) {
        MaintenanceResponseDTO maintenance = maintenanceService.getMaintenanceByCode(maintenanceId);
        addLinks(maintenance);
        return ResponseEntity.ok(maintenance);
    }

    @Operation(summary = "Get maintenance reports by crew group", description = "Retrieves maintenance reports assigned to a specific crew group.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved maintenance reports"),
        @ApiResponse(responseCode = "404", description = "No maintenance reports found for this crew")
    })
    @GetMapping("/crew/{crewId}")
    public ResponseEntity<CollectionModel<MaintenanceResponseDTO>> getMaintenancesByCrewId(
            @Parameter(description = "Crew group ID", example = "CREW-A") @PathVariable String crewId) {
        List<MaintenanceResponseDTO> maintenances = maintenanceService.getMaintenancesByCrewId(crewId);
        maintenances.forEach(this::addLinks);

        CollectionModel<MaintenanceResponseDTO> collectionModel = CollectionModel.of(maintenances);
        collectionModel.add(linkTo(methodOn(MaintenanceController.class).getMaintenancesByCrewId(crewId)).withSelfRel());
        collectionModel.add(linkTo(methodOn(MaintenanceController.class).getAllMaintenances()).withRel("all-maintenances"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Create a new maintenance report", description = "Creates a new maintenance report record.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Maintenance report created successfully", content = @Content(schema = @Schema(implementation = MaintenanceResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<MaintenanceResponseDTO> createMaintenance(@Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        MaintenanceResponseDTO saved = maintenanceService.createMaintenance(requestDTO);
        addLinks(saved);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Update an existing maintenance report", description = "Updates an existing maintenance report record.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Maintenance report updated successfully", content = @Content(schema = @Schema(implementation = MaintenanceResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Maintenance report not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> updateMaintenance(
            @Parameter(description = "Maintenance ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        MaintenanceResponseDTO updated = maintenanceService.updateMaintenance(id, requestDTO);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a maintenance report", description = "Deletes a maintenance report record based on ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Maintenance report deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Maintenance report not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(
            @Parameter(description = "Maintenance ID", example = "1") @PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(MaintenanceResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(MaintenanceController.class).getMaintenanceById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(MaintenanceController.class).getAllMaintenances()).withRel("all-maintenances"));
        }
    }
}