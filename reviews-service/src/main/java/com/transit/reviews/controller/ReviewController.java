package com.transit.reviews.controller;

import com.transit.reviews.dto.request.ReviewRequestDTO;
import com.transit.reviews.dto.response.ReviewResponseDTO;
import com.transit.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Operations pertaining to user reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Grabs every review record there is.", description = "Returns all stored reviews in the db.")
    @GetMapping
    public ResponseEntity<CollectionModel<ReviewResponseDTO>> getAllReviews() {
        List<ReviewResponseDTO> reviews = reviewService.getAllReviews();
        reviews.forEach(this::addLinks);

        CollectionModel<ReviewResponseDTO> collectionModel = CollectionModel.of(reviews);
        collectionModel.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Fetches a review by its ID.", description = "Retrieves a specific review based on its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        ReviewResponseDTO review = reviewService.getReviewById(id);
        addLinks(review);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Fetches a review by the client ID.", description = "Retrieves reviews based on the associated client's unique identifier.")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<CollectionModel<ReviewResponseDTO>> getReviewsByClientId(@PathVariable Long clientId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByClientId(clientId);
        reviews.forEach(this::addLinks);

        CollectionModel<ReviewResponseDTO> collectionModel = CollectionModel.of(reviews);
        collectionModel.add(linkTo(methodOn(ReviewController.class).getReviewsByClientId(clientId)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("all-reviews"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Fetches a review by the specific train ID.", description = "Retrieves reviews based on the associated train's unique identifier.")
    @GetMapping("/train/{trainId}")
    public ResponseEntity<CollectionModel<ReviewResponseDTO>> getReviewsByTrainId(@PathVariable Long trainId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByTrainId(trainId);
        reviews.forEach(this::addLinks);

        CollectionModel<ReviewResponseDTO> collectionModel = CollectionModel.of(reviews);
        collectionModel.add(linkTo(methodOn(ReviewController.class).getReviewsByTrainId(trainId)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("all-reviews"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Fetches every review with a provided rating.", description = "Retrieves a list of reviews that match the provided rating value (1-5).")
    @GetMapping("/rating/{rating}")
    public ResponseEntity<CollectionModel<ReviewResponseDTO>> getReviewsByRating(@PathVariable Integer rating) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByRating(rating);
        reviews.forEach(this::addLinks);

        CollectionModel<ReviewResponseDTO> collectionModel = CollectionModel.of(reviews);
        collectionModel.add(linkTo(methodOn(ReviewController.class).getReviewsByRating(rating)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("all-reviews"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Creates a new review entry.", description = "Creates a new review record in the database utilizing data protection and validation (@Min, @Max, etc).")
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> saveReview(@Valid @RequestBody ReviewRequestDTO requestDTO) {
        ReviewResponseDTO savedReview = reviewService.createReview(requestDTO);
        addLinks(savedReview);
        return ResponseEntity.ok(savedReview);
    }

    @Operation(summary = "Deletes an entry.", description = "Deletes a review record from the database based on the provided ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(ReviewResponseDTO review) {
        if (review.getId() != null && review.getId() > 0) {
            review.add(linkTo(methodOn(ReviewController.class).getReviewById(review.getId())).withSelfRel());
            review.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("all-reviews"));
        }
    }
}