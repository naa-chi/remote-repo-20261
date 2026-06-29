package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.train;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class trainAssembler implements RepresentationModelAssembler<train, EntityModel<train>> {

    @Override
    public EntityModel<train> toModel(train entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(trainController.class).getTrainById(entity.getSpecificTrainId())).withSelfRel(),
                linkTo(methodOn(trainController.class).getAll()).withRel("allTrains"),
                linkTo(methodOn(trainController.class).getByCode(entity.getCode())).withRel("byCode"),
                linkTo(methodOn(trainController.class).getByManufacturerId(entity.getManufacturerId())).withRel("byManufacturer"),
                linkTo(methodOn(trainController.class).getByTypeTrain(entity.getTypeTrain() != null ? entity.getTypeTrain().getId() : null)).withRel("byType"));
    }

    @Override
    public CollectionModel<EntityModel<train>> toCollectionModel(Iterable<? extends train> entities) {
        CollectionModel<EntityModel<train>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(trainController.class).getAll()).withSelfRel());
        return collection;
    }
}