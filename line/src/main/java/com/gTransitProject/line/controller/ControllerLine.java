package com.gTransitProject.line.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.line.model.Line;
import com.gTransitProject.line.service.ServiceLine;

@RestController
@RequestMapping("/api/lines")
public class ControllerLine {

    @Autowired
    private ServiceLine lineServ;

    @GetMapping
    public List<Line> getLines(){
        return lineServ.getLines();
    }

    @GetMapping("/{id}")
    public Line getLineById(@PathVariable Integer id){
        return lineServ.getLineById(id);
    }

    @GetMapping("/number/{lineNumber}")
public Line getLineByNumber(
        @PathVariable Integer lineNumber){

    return lineServ.getLineByNumber(lineNumber);
}
    @PostMapping
    public Line saveLine(@RequestBody Line line){
        return lineServ.saveLine(line);
    }

    @PutMapping("/{id}")
    public Line updateLine(@PathVariable Integer id,
                           @RequestBody Line line){

        return lineServ.updateLine(id, line);
    }

    @DeleteMapping("/{id}")
    public void deleteLine(@PathVariable Integer id){
        lineServ.deleteLine(id);
    }
}