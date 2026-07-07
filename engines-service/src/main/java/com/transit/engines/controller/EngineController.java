package com.transit.engines.controller;

import com.transit.engines.dto.request.EngineRequestDTO;
import com.transit.engines.dto.response.EngineResponseDTO;
import com.transit.engines.service.EngineService;
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
@RequestMapping("/api/engines")
@Tag(name = "Engines", description = "Operations pertaining to rolling stock engines management")
public class EngineController {

    private final EngineService engineService;

    public EngineController(EngineService engineService) {
        this.engineService = engineService;
    }

    @Operation(summary = "Get an engine by ID", description = "Fetches a single engine record by its primary key database ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the engine record",
                    content = @Content(schema = @Schema(implementation = EngineResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Engine not found with the specified database ID", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EngineResponseDTO> getEngineById(
            @Parameter(description = "Primary key database ID of the engine record", required = true, example = "1")
            @PathVariable Long id) {
        EngineResponseDTO engine = engineService.getEngineById(id);
        engine.add(linkTo(methodOn(EngineController.class).getEngineById(id)).withSelfRel());
        engine.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));
        return ResponseEntity.ok(engine);
    }

    @Operation(summary = "Get a list of engines by manufacturer", description = "Retrieves a collection of engines built by a specific manufacturer string ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of engines",
                    content = @Content(schema = @Schema(implementation = CollectionModel.class))),
            @ApiResponse(responseCode = "404", description = "No engines found for the specified manufacturer", content = @Content)
    })
    @GetMapping("/manufacturer/{code}")
    public ResponseEntity<CollectionModel<EngineResponseDTO>> getEnginesByManufacturerCode(
            @Parameter(description = "The unique string code identifying the manufacturer", required = true, example = "M123")
            @PathVariable String code) {
        List<EngineResponseDTO> engines = engineService.getEnginesByManufacturerId(code);
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineResponseDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByManufacturerCode(code)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get a list of engines by engine code", description = "Retrieves engines matching a specific engineering model code name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved engine list matched by engine code"),
            @ApiResponse(responseCode = "404", description = "No engines matching the specified engine code were found")
    })
    @GetMapping("/engineCode/{engineCode}")
    public ResponseEntity<CollectionModel<EngineResponseDTO>> getEnginesByEngineCode(
            @Parameter(description = "The model classification code of the engine unit", required = true, example = "V8-Turbo")
            @PathVariable String engineCode) {
        List<EngineResponseDTO> engines = engineService.getEnginesByEngineCode(engineCode);
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineResponseDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByEngineCode(engineCode)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get a list of engines by horsepower", description = "Retrieves engines that match the specified exact horsepower metric rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched matching horsepower records"),
            @ApiResponse(responseCode = "404", description = "No engine assets match the target horsepower specification")
    })
    @GetMapping("/horsepower/{horsepower}")
    public ResponseEntity<CollectionModel<EngineResponseDTO>> getEnginesByHorsepower(
            @Parameter(description = "Exact float calculation score of the engine power", required = true, example = "4400.0")
            @PathVariable Float horsepower) {
        List<EngineResponseDTO> engines = engineService.getEngineHorsepower(horsepower);
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineResponseDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByHorsepower(horsepower)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get a list of engines by price", description = "Retrieves engine units registered under an exact fiscal pricing figure.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully matched price listings"),
            @ApiResponse(responseCode = "404", description = "No registered assets exactly hit that exact price indicator")
    })
    @GetMapping("/price/{price}")
    public ResponseEntity<CollectionModel<EngineResponseDTO>> getEnginesByPrice(
            @Parameter(description = "Exact unit pricing cost assigned to the asset model", required = true, example = "150000.0")
            @PathVariable Float price) {
        List<EngineResponseDTO> engines = engineService.getEnginePrice(price);
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineResponseDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByPrice(price)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get a list of engines by weight", description = "Filters engine inventory models by their specific weight classification target.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully filtered stock list by engine weight parameters"),
            @ApiResponse(responseCode = "404", description = "No matching records indexed near that mass rating")
    })
    @GetMapping("/weight/{weight}")
    public ResponseEntity<CollectionModel<EngineResponseDTO>> getEnginesByWeight(
            @Parameter(description = "Total target unit physical weight metrics scale description", required = true, example = "120.5")
            @PathVariable Float weight) {
        List<EngineResponseDTO> engines = engineService.getEngineWeight(weight);
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineResponseDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getEnginesByWeight(weight)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withRel("all-engines"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get all engines", description = "Fetches a full collection registry listing of every recorded engine structure.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned absolute system catalog mapping records"),
            @ApiResponse(responseCode = "404", description = "The database repository has no registered records stored")
    })
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EngineResponseDTO>> getAllEngines() {
        List<EngineResponseDTO> engines = engineService.getAllEngines();
        engines.forEach(this::addItemLinks);

        CollectionModel<EngineResponseDTO> collectionModel = CollectionModel.of(engines);
        collectionModel.add(linkTo(methodOn(EngineController.class).getAllEngines()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Create a new engine", description = "Persists a new entry to the rolling asset engine database mapping layer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset unit mapping initialized successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid constraints schema provided via submission mapping input payload verification block")
    })
    @PostMapping("/create")
    public ResponseEntity<EngineResponseDTO> createEngine(
            @Parameter(description = "Validated request body content for manufacturing payload model", required = true)
            @Valid @RequestBody EngineRequestDTO engineDTO) {
        EngineResponseDTO created = engineService.createEngine(engineDTO);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update an engine asset resource", description = "Updates fields of an existing engine mapping configuration tracking index by its primary location key.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset details fully synchronized successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid schema fields execution arguments caught by framework evaluation checks"),
            @ApiResponse(responseCode = "404", description = "The target record to overwrite does not exist in backend tracking storage databases")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EngineResponseDTO> updateEngine(
            @Parameter(description = "Primary target system mapping key used during indexing identification workflows", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Payload specifications reflecting changes targeted across table entities", required = true)
            @Valid @RequestBody EngineRequestDTO engineDTO) {
        return ResponseEntity.ok(engineService.updateEngine(id, engineDTO));
    }

    @Operation(summary = "Delete an engine entity context", description = "Removes an asset registration mapping systematically out from active storage index configurations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "System processing finalized; context database dropped target content index completely with no contents returning back"),
            @ApiResponse(responseCode = "404", description = "No deletion process target context mapped on that identity index reference location context")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEngine(
            @Parameter(description = "The target entry persistence identifier tracking code row index position used mapping data removal execution blocks", required = true, example = "1")
            @PathVariable Long id) {
        engineService.deleteEngine(id);
        return ResponseEntity.noContent().build();
    }

    private void addItemLinks(EngineResponseDTO engine) {
        if (engine.getId() != null && engine.getId() > 0) {
            engine.add(linkTo(methodOn(EngineController.class).getEngineById(engine.getId())).withSelfRel());
        }
    }
}