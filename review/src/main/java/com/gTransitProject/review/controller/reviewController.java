package com.gTransitProject.review.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gTransitProject.review.model.reviewModel;
import com.gTransitProject.review.service.reviewService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/reviews")
public class reviewController {
    @Autowired
    private reviewService service;

    @Operation(summary = "Grabs every record there is.", description = "Calls a method that returns all stored reviews in the db. ")
    @GetMapping
    public List<reviewModel> getReviews(){
        return service.getAll();
    }

    @Operation(summary = "Fetches a review by its ID.", description = "Retrieves a specific review based on its unique identifier.")
    @GetMapping("/{id}")
    public reviewModel getReviewById(@PathVariable Integer id){
        return service.getById(id);
    }

    @Operation(summary = "Fetches a review by the client ID.", description = "Retrieves a specific review based on the associated client's unique identifier.")
    @GetMapping("/client/{clientId}")
    public reviewModel getReviewByClientId(@PathVariable Integer clientId){
        return service.getByClientId(clientId);
    }

    @Operation(summary = "Fetches a review by the specific train ID.", description = "Retrieves a specific review based on the associated train's unique identifier.")
    @GetMapping("/train/{trainId}")
    public reviewModel getReviewBySpecificTrainId(@PathVariable Integer trainId){
        return service.getBySpecificTrainId(trainId);
    }

    @Operation(summary = "Fetches every review with a provided rating out of 5.", description = "Retrieves a list of reviews that match the provided rating value.")
    @GetMapping("/rating/{rating}")
    public List<reviewModel> getReviewByRating(@PathVariable Integer rating){
        return service.getByRating(rating);
    }


    @Operation(summary = "Creates a new review entry.", description = "Creates a new review record in the database using the provided data.")
    @PostMapping
    public reviewModel saveReview(@RequestBody reviewModel review){
        return service.createReview(review);
    }

    @Operation(summary = "Deletes an entry.", description = "Deletes a review record from the database based on the provided ID.")
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Integer id){
        service.deleteReview(id);
    }
}
