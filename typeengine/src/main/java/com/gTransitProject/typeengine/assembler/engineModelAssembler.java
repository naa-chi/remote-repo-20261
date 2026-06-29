package com.gTransitProject.typeengine.assembler;

import com.gTransitProject.typeengine.controller.typeEngineController;
import com.gTransitProject.typeengine.model.typeEngine;
import com.gTransitProject.typeengine.typeEngineDTO.engineDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class engineModelAssembler implements RepresentationModelAssembler<typeEngine, EntityModel<engineDTO>> {

    @Override
    public EntityModel<engineDTO> toModel(typeEngine entity) {
        // Map entity → DTO
        engineDTO dto = new engineDTO();
        dto.setId(entity.getId());
        dto.setTypeCodeEngine(entity.getTypeCodeEngine());
        dto.setHorsepower(entity.getHorsepower());

        // Build HATEOAS links
        EntityModel<engineDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(typeEngineController.class).getById(entity.getId())).withSelfRel());
        model.add(linkTo(methodOn(typeEngineController.class).getAll()).withRel("all-engines"));
        // Optional: add update/delete links if needed
        model.add(linkTo(methodOn(typeEngineController.class).updateTypeEngine(entity.getId(), null)).withRel("update"));
        model.add(linkTo(methodOn(typeEngineController.class).deleteTypeEngine(entity.getId())).withRel("delete"));

        return model;
    }
}