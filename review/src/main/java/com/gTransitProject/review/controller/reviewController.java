package com.gTransitProject.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.review.model.reviewModel;
import com.gTransitProject.review.service.reviewService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/reviews")
public class reviewController {

    @Autowired
    private reviewService service;

    @Autowired
    private reviewAssembler assembler;

    @Operation(summary = "Grabs every record there is.", description = "Calls a method that returns all stored reviews in the db.")
    @GetMapping
    public CollectionModel<EntityModel<reviewModel>> getReviews() {
        return assembler.toCollectionModel(service.getAll());
    }

    @Operation(summary = "Fetches a review by its ID.", description = "Retrieves a specific review based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<reviewModel>> getReviewById(@PathVariable Integer id) {
        reviewModel review = service.getById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(review));
    }

    @Operation(summary = "Fetches a review by the client ID.", description = "Retrieves a specific review based on the associated client's unique identifier.")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<EntityModel<reviewModel>> getReviewByClientId(@PathVariable String clientId) {
        reviewModel review = service.getByClientId(clientId);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(review));
    }

    @Operation(summary = "Fetches a review by the specific train ID.", description = "Retrieves a specific review based on the associated train's unique identifier.")
    @GetMapping("/train/{trainId}")
    public ResponseEntity<EntityModel<reviewModel>> getReviewBySpecificTrainId(@PathVariable Integer trainId) {
        reviewModel review = service.getBySpecificTrainId(trainId);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(review));
    }

    @Operation(summary = "Fetches every review with a provided rating out of 5.", description = "Retrieves a list of reviews that match the provided rating value.")
    @GetMapping("/rating/{rating}")
    public CollectionModel<EntityModel<reviewModel>> getReviewByRating(@PathVariable Integer rating) {
        List<reviewModel> reviews = service.getByRating(rating);
        return assembler.toCollectionModel(reviews);
    }

    @Operation(summary = "Creates a new review entry.", description = "Creates a new review record in the database using the provided data.")
    @PostMapping
    public ResponseEntity<EntityModel<reviewModel>> saveReview(@RequestBody reviewModel review) {
        reviewModel saved = service.createReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(summary = "Deletes an entry.", description = "Deletes a review record from the database based on the provided ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        service.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}