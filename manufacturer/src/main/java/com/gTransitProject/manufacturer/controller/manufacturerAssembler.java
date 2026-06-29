package com.gTransitProject.manufacturer.controller;

import com.gTransitProject.manufacturer.model.manufacturerModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class manufacturerAssembler implements RepresentationModelAssembler<manufacturerModel, EntityModel<manufacturerModel>> {

    @Override
    public EntityModel<manufacturerModel> toModel(manufacturerModel entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(manufacturerController.class).findById(entity.getId())).withSelfRel(),
                linkTo(methodOn(manufacturerController.class).findAll()).withRel("allManufacturers"));
    }

    @Override
    public CollectionModel<EntityModel<manufacturerModel>> toCollectionModel(Iterable<? extends manufacturerModel> entities) {
        CollectionModel<EntityModel<manufacturerModel>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(manufacturerController.class).findAll()).withSelfRel());
        return collection;
    }
}