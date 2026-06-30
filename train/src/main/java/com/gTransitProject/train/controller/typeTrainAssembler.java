package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.typeTrain;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class typeTrainAssembler implements RepresentationModelAssembler<typeTrain, EntityModel<typeTrain>> {

    @Override
    public EntityModel<typeTrain> toModel(typeTrain entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(typeTrainController.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(typeTrainController.class).getAll()).withRel("allTypes"));
    }

    @Override
    public CollectionModel<EntityModel<typeTrain>> toCollectionModel(Iterable<? extends typeTrain> entities) {
        CollectionModel<EntityModel<typeTrain>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(typeTrainController.class).getAll()).withSelfRel());
        return collection;
    }
}