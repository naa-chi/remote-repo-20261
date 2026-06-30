package com.gTransitProject.ticket.controller;

import com.gTransitProject.ticket.model.ticketModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ticketAssembler implements RepresentationModelAssembler<ticketModel, EntityModel<ticketModel>> {

    @Override
    public EntityModel<ticketModel> toModel(ticketModel entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ticketController.class).getTicketById(entity.getTicketId())).withSelfRel(),
                linkTo(methodOn(ticketController.class).getTickets()).withRel("allTickets"));
    }

    @Override
    public CollectionModel<EntityModel<ticketModel>> toCollectionModel(Iterable<? extends ticketModel> entities) {
        CollectionModel<EntityModel<ticketModel>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(ticketController.class).getTickets()).withSelfRel());
        return collection;
    }
}