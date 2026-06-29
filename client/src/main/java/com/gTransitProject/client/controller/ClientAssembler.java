package com.gTransitProject.client.controller;

import com.gTransitProject.client.model.client;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClientAssembler implements RepresentationModelAssembler<client, EntityModel<client>> {

    @Override
    public EntityModel<client> toModel(client entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ClientController.class).getClientById(entity.getClientId())).withSelfRel(),
                linkTo(methodOn(ClientController.class).getAllClients()).withRel("allClients"));
    }

    @Override
    public CollectionModel<EntityModel<client>> toCollectionModel(Iterable<? extends client> entities) {
        CollectionModel<EntityModel<client>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(ClientController.class).getAllClients()).withSelfRel());
        return collection;
    }
}