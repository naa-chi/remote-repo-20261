package com.transit.cities.controller;

import com.transit.cities.dto.request.CityRequestDTO;
import com.transit.cities.dto.response.CityResponseDTO;
import com.transit.cities.service.CityService;
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
@RequestMapping("/api/cities")
@Tag(name = "Cities", description = "Operations on cities")
public class CityController {
    private final CityService service;

    public CityController(CityService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<CollectionModel<CityResponseDTO>> getAll() {
        List<CityResponseDTO> list = service.getAllCities();
        list.forEach(this::addLinks);
        CollectionModel<CityResponseDTO> cm = CollectionModel.of(list);
        cm.add(linkTo(methodOn(CityController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(cm);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponseDTO> getById(@PathVariable Long id) {
        CityResponseDTO dto = service.getCityById(id);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/code/{cityCode}")
    public ResponseEntity<CityResponseDTO> getByCode(@PathVariable String cityCode) {
        CityResponseDTO dto = service.getCityByCode(cityCode);
        addLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CityResponseDTO> create(@Valid @RequestBody CityRequestDTO request) {
        CityResponseDTO created = service.createCity(request);
        addLinks(created);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CityRequestDTO request) {
        CityResponseDTO updated = service.updateCity(id, request);
        addLinks(updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    @Operation(summary = "Get all cities with HATEOAS links")
    public ResponseEntity<CollectionModel<CityResponseDTO>> getAllCitiesWithLinks() {
        List<CityResponseDTO> cities = service.getAllCities();
        cities.forEach(this::addLinks);
        CollectionModel<CityResponseDTO> collectionModel = CollectionModel.of(cities);
        collectionModel.add(linkTo(methodOn(CityController.class).getAllCitiesWithLinks()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    private void addLinks(CityResponseDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            dto.add(linkTo(methodOn(CityController.class).getById(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(CityController.class).getAll()).withRel("all-cities"));
        }
    }
}