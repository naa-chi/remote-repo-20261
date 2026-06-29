package com.gTransitProject.city.controller;

import com.gTransitProject.city.model.City;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CityAssembler implements RepresentationModelAssembler<City, EntityModel<City>> {

    @Override
    public EntityModel<City> toModel(City entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CityController.class).getCityByCode(entity.getCityCode())).withSelfRel(),
                linkTo(methodOn(CityController.class).getAllCities()).withRel("allCities"));
    }

    @Override
    public CollectionModel<EntityModel<City>> toCollectionModel(Iterable<? extends City> entities) {
        CollectionModel<EntityModel<City>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(CityController.class).getAllCities()).withSelfRel());
        return collection;
    }
}