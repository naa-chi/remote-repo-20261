package com.gTransitProject.line.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.line.model.Line;
import com.gTransitProject.line.service.ServiceLine;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/lines")
public class ControllerLine {

    @Autowired
    private ServiceLine lineServ;

    @Operation(summary = "Get all lines", description = "Retrieves a list of all lines in the database.")
    @GetMapping
    public List<Line> getLines(){
        return lineServ.getLines();
    }

    @Operation(summary = "Get a line by ID", description = "Retrieves a specific line based on its unique identifier.")
    @GetMapping("/{id}")
    public Line getLineById(@PathVariable Integer id){
        return lineServ.getLineById(id);
    }

    @Operation(summary = "Get a line by number", description = "Retrieves a specific line based on its unique number.")
    @GetMapping("/number/{lineNumber}")
public Line getLineByNumber(
        @PathVariable Integer lineNumber){

    return lineServ.getLineByNumber(lineNumber);
}
    @Operation(summary = "Create a new line", description = "Creates a new line record in the database with the provided data.")
    @PostMapping
    public Line saveLine(@RequestBody Line line){
        return lineServ.saveLine(line);
    }

    @Operation(summary = "Update a line", description = "Updates an existing line record in the database with the provided data.")
    @PutMapping("/{id}")
    public Line updateLine(@PathVariable Integer id,
                           @RequestBody Line line){

        return lineServ.updateLine(id, line);
    }

    @Operation(summary = "Delete a line", description = "Deletes an existing line record from the database based on its unique identifier.")
    @DeleteMapping("/{id}")
    public void deleteLine(@PathVariable Integer id){
        lineServ.deleteLine(id);
    }
}