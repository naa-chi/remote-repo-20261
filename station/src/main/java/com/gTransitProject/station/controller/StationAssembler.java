package com.gTransitProject.station.controller;

import com.gTransitProject.station.model.station;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class StationAssembler implements RepresentationModelAssembler<station, EntityModel<station>> {

    @Override
    public EntityModel<station> toModel(station entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ControllerStation.class).getStationById(entity.getStationId())).withSelfRel(),
                linkTo(methodOn(ControllerStation.class).getStations()).withRel("allStations"));
    }

    @Override
    public CollectionModel<EntityModel<station>> toCollectionModel(Iterable<? extends station> entities) {
        CollectionModel<EntityModel<station>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(ControllerStation.class).getStations()).withSelfRel());
        return collection;
    }
}