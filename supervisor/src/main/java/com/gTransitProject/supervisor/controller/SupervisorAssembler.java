package com.gTransitProject.supervisor.controller;

import com.gTransitProject.supervisor.model.Supervisor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SupervisorAssembler implements RepresentationModelAssembler<Supervisor, EntityModel<Supervisor>> {

    @Override
    public EntityModel<Supervisor> toModel(Supervisor entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(SupervisorController.class).getSupervisorById(entity.getSupervisorId())).withSelfRel(),
                linkTo(methodOn(SupervisorController.class).getAllSupervisors()).withRel("allSupervisors"));
    }

    @Override
    public CollectionModel<EntityModel<Supervisor>> toCollectionModel(Iterable<? extends Supervisor> entities) {
        CollectionModel<EntityModel<Supervisor>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(SupervisorController.class).getAllSupervisors()).withSelfRel());
        return collection;
    }
}