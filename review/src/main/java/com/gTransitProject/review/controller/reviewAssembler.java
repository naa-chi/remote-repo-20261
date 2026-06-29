package com.gTransitProject.review.controller;

import com.gTransitProject.review.model.reviewModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class reviewAssembler implements RepresentationModelAssembler<reviewModel, EntityModel<reviewModel>> {

    @Override
    public EntityModel<reviewModel> toModel(reviewModel entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(reviewController.class).getReviewById(entity.getId())).withSelfRel(),
                linkTo(methodOn(reviewController.class).getReviews()).withRel("allReviews"));
    }

    @Override
    public CollectionModel<EntityModel<reviewModel>> toCollectionModel(Iterable<? extends reviewModel> entities) {
        CollectionModel<EntityModel<reviewModel>> collection = RepresentationModelAssembler.super.toCollectionModel(entities);
        collection.add(linkTo(methodOn(reviewController.class).getReviews()).withSelfRel());
        return collection;
    }
}