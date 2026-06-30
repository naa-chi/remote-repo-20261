package com.gTransitProject.maintenance.controller;

import com.gTransitProject.maintenance.model.maintenanceModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class maintenanceAssembler implements RepresentationModelAssembler<maintenanceModel, EntityModel<maintenanceModel>> {

    @Override
    public EntityModel<maintenanceModel> toModel(maintenanceModel entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(maintenanceController.class).findById(entity.getId())).withSelfRel(),
                linkTo(methodOn(maintenanceController.class).findAll()).withRel("allMaintenances"));
    }

    @Override
    public CollectionModel<EntityModel<maintenanceModel>> toCollectionModel(Iterable<? extends maintenanceModel> entities) {
        CollectionModel<EntityModel<maintenanceModel>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(maintenanceController.class).findAll()).withSelfRel());
        return collection;
    }
}