package com.transit.reviews.controller;

import com.transit.reviews.dto.request.ReviewRequestDTO;
import com.transit.reviews.dto.response.ReviewResponseDTO;
import com.transit.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all reviews", description = "Returns all stored reviews in the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all reviews"),
        @ApiResponse(responseCode = "404", description = "No reviews found")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<ReviewResponseDTO>> getAllReviews() {
        List<ReviewResponseDTO> reviews = reviewService.getAllReviews();
        reviews.forEach(this::addLinks);

        CollectionModel<ReviewResponseDTO> collectionModel = CollectionModel.of(reviews);
        collectionModel.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get review by ID", description = "Retrieves a specific review based on its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved review", content = @Content(schema = @Schema(implementation = ReviewResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@Parameter(description = "Review ID", example = "1") @PathVariable Long id) {
        ReviewResponseDTO review = reviewService.getReviewById(id);
        addLinks(review);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Get reviews by client ID", description = "Retrieves reviews based on the associated client's unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews"),
        @ApiResponse(responseCode = "404", description = "No reviews found for this client")
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<CollectionModel<ReviewResponseDTO>> getReviewsByClientId(
            @Parameter(description = "Client ID", example = "1") @PathVariable Long clientId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByClientId(clientId);
        reviews.forEach(this::addLinks);

        CollectionModel<ReviewResponseDTO> collectionModel = CollectionModel.of(reviews);
        collectionModel.add(linkTo(methodOn(ReviewController.class).getReviewsByClientId(clientId)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("all-reviews"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get reviews by train ID", description = "Retrieves reviews based on the associated train's unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews"),
        @ApiResponse(responseCode = "404", description = "No reviews found for this train")
    })
    @GetMapping("/train/{trainId}")
    public ResponseEntity<CollectionModel<ReviewResponseDTO>> getReviewsByTrainId(
            @Parameter(description = "Train ID", example = "1") @PathVariable Long trainId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByTrainId(trainId);
        reviews.forEach(this::addLinks);

        CollectionModel<ReviewResponseDTO> collectionModel = CollectionModel.of(reviews);
        collectionModel.add(linkTo(methodOn(ReviewController.class).getReviewsByTrainId(trainId)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("all-reviews"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get reviews by rating", description = "Retrieves a list of reviews that match the provided rating value (1-5).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews"),
        @ApiResponse(responseCode = "404", description = "No reviews found with this rating")
    })
    @GetMapping("/rating/{rating}")
    public ResponseEntity<CollectionModel<ReviewResponseDTO>> getReviewsByRating(
            @Parameter(description = "Rating value (1-5)", example = "5") @PathVariable Integer rating) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByRating(rating);
        reviews.forEach(this::addLinks);

        CollectionModel<ReviewResponseDTO> collectionModel = CollectionModel.of(reviews);
        collectionModel.add(linkTo(methodOn(ReviewController.class).getReviewsByRating(rating)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("all-reviews"));
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Create a new review", description = "Creates a new review record in the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review created successfully", content = @Content(schema = @Schema(implementation = ReviewResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Referenced ticket or train not found")
    })
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO requestDTO) {
        ReviewResponseDTO savedReview = reviewService.createReview(requestDTO);
        addLinks(savedReview);
        return ResponseEntity.ok(savedReview);
    }

    @Operation(summary = "Delete a review", description = "Deletes a review record from the database based on the provided ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@Parameter(description = "Review ID", example = "1") @PathVariable Long id) {
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