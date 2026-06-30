package com.gTransitProject.line.controller;

import com.gTransitProject.line.model.Line;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class LineAssembler implements RepresentationModelAssembler<Line, EntityModel<Line>> {

    @Override
    public EntityModel<Line> toModel(Line entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ControllerLine.class).getLineById(entity.getId())).withSelfRel(),
                linkTo(methodOn(ControllerLine.class).getLines()).withRel("allLines"));
    }

    @Override
    public CollectionModel<EntityModel<Line>> toCollectionModel(Iterable<? extends Line> entities) {
        CollectionModel<EntityModel<Line>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(ControllerLine.class).getLines()).withSelfRel());
        return collection;
    }
}