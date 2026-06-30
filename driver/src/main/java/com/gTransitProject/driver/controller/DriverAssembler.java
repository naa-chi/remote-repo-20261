package com.gTransitProject.driver.controller;

import com.gTransitProject.driver.model.driverModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DriverAssembler implements RepresentationModelAssembler<driverModel, EntityModel<driverModel>> {

    @Override
    public EntityModel<driverModel> toModel(driverModel entity) {
        // Convert Integer ID to Long for link generation (controller uses Long)
        Long id = entity.getDriverId().longValue();
        return EntityModel.of(entity,
                linkTo(methodOn(driverController.class).getDriverById(id)).withSelfRel(),
                linkTo(methodOn(driverController.class).getAllDrivers()).withRel("allDrivers"));
    }

    @Override
    public CollectionModel<EntityModel<driverModel>> toCollectionModel(Iterable<? extends driverModel> entities) {
        CollectionModel<EntityModel<driverModel>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(driverController.class).getAllDrivers()).withSelfRel());
        return collection;
    }
}