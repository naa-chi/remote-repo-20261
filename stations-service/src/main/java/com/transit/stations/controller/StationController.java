package com.transit.stations.controller;

import com.transit.stations.dto.request.StationRequestDTO;
import com.transit.stations.dto.response.StationResponseDTO;
import com.transit.stations.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SuppressWarnings("all")

@RestController
@RequestMapping("/api/stations")
@Tag(name = "Stations", description = "Station management")
public class StationController {

    private final StationService service;

    public StationController(StationService service) {
        this.service = service;
    }

    @Operation(summary = "Get all stations")
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<StationResponseDTO>> getAll() {
        List<StationResponseDTO> stations = service.getAllStations();
        stations.forEach(this::addLinks);
        CollectionModel<StationResponseDTO> collection = CollectionModel.of(stations);
        collection.add(linkTo(methodOn(StationController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Get station by ID")
    @GetMapping("/{id}")
    public ResponseEntity<StationResponseDTO> getById(@PathVariable Long id) {
        StationResponseDTO dto = service.getStationById(id);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get station by station code")
    @GetMapping("/code/{stationCode}")
    public ResponseEntity<StationResponseDTO> getByCode(@PathVariable String stationCode) {
        StationResponseDTO dto = service.getStationByCode(stationCode);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get stations by city code")
    @GetMapping("/city/{cityCode}")
    public ResponseEntity<CollectionModel<StationResponseDTO>> getByCity(@PathVariable String cityCode) {
        List<StationResponseDTO> stations = service.getStationsByCity(cityCode);
        stations.forEach(this::addLinks);
        CollectionModel<StationResponseDTO> collection = CollectionModel.of(stations);
        collection.add(linkTo(methodOn(StationController.class).getByCity(cityCode)).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Get stations by line code (any of the 4 line slots)")
    @GetMapping("/line/{lineCode}")
    public ResponseEntity<CollectionModel<StationResponseDTO>> getByLine(@PathVariable Integer lineCode) {
        List<StationResponseDTO> stations = service.getStationsByLine(lineCode);
        stations.forEach(this::addLinks);
        CollectionModel<StationResponseDTO> collection = CollectionModel.of(stations);
        collection.add(linkTo(methodOn(StationController.class).getByLine(lineCode)).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Create a new station")
    @PostMapping
    public ResponseEntity<StationResponseDTO> create(@Valid @RequestBody StationRequestDTO request) {
        StationResponseDTO created = service.createStation(request);
        addLinks(created);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update an existing station")
    @PutMapping("/{id}")
    public ResponseEntity<StationResponseDTO> update(@PathVariable Long id, @Valid @RequestBody StationRequestDTO request) {
        StationResponseDTO updated = service.updateStation(id, request);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a station")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteStation(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(StationResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(StationController.class).getById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(StationController.class).getAll()).withRel("all-stations"));
        }
    }
}