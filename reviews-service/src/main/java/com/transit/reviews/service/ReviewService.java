package com.transit.reviews.service;

import com.transit.reviews.assembler.ReviewMapper;
import com.transit.reviews.dto.request.ReviewRequestDTO;
import com.transit.reviews.dto.response.ReviewResponseDTO;
import com.transit.reviews.fallback.ReviewServiceFallback;
import com.transit.reviews.model.ReviewModel;
import com.transit.reviews.repository.ReviewsRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewsRepository repository;
    private final ReviewMapper mapper;
    private final ReviewServiceFallback serviceFallback;

    public ReviewService(ReviewsRepository repository,
                         ReviewMapper mapper,
                         ReviewServiceFallback serviceFallback) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceFallback = serviceFallback;
    }

    @CircuitBreaker(name = "reviewService", fallbackMethod = "handleGetReviewFallback")
    public ReviewResponseDTO getReviewById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    @CircuitBreaker(name = "reviewService", fallbackMethod = "handleGetReviewsByClientIdFallback")
    public List<ReviewResponseDTO> getReviewsByClientId(Long clientId) {
        List<ReviewModel> reviews = repository.findByClientId(clientId);
        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found for client ID: " + clientId);
        }
        return reviews.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "reviewService", fallbackMethod = "handleGetReviewsByTrainIdFallback")
    public List<ReviewResponseDTO> getReviewsByTrainId(Long trainId) {
        List<ReviewModel> reviews = repository.findByTrainId(trainId);
        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found for train ID: " + trainId);
        }
        return reviews.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "reviewService", fallbackMethod = "handleGetReviewsByRatingFallback")
    public List<ReviewResponseDTO> getReviewsByRating(Integer rating) {
        List<ReviewModel> reviews = repository.findByRating(rating);
        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found with rating: " + rating);
        }
        return reviews.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "reviewService", fallbackMethod = "handleGetAllReviewsFallback")
    public List<ReviewResponseDTO> getAllReviews() {
        List<ReviewModel> reviews = repository.findAll();
        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found in the database.");
        }
        return reviews.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @CircuitBreaker(name = "reviewService", fallbackMethod = "handleGetReviewFallback")
    public ReviewResponseDTO createReview(ReviewRequestDTO requestDTO) {
        ReviewModel model = mapper.toEntity(requestDTO);
        return mapper.toResponse(repository.save(model));
    }

    @CircuitBreaker(name = "reviewService", fallbackMethod = "handleGetReviewFallback")
    public void deleteReview(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Review not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private ReviewResponseDTO handleGetReviewFallback(Long id, Throwable t) {
        return serviceFallback.getReviewFallback(id, t);
    }

    private List<ReviewResponseDTO> handleGetReviewsByClientIdFallback(Long clientId, Throwable t) {
        return serviceFallback.getReviewsByClientIdFallback(clientId, t);
    }

    private List<ReviewResponseDTO> handleGetReviewsByTrainIdFallback(Long trainId, Throwable t) {
        return serviceFallback.getReviewsByTrainIdFallback(trainId, t);
    }

    private List<ReviewResponseDTO> handleGetReviewsByRatingFallback(Integer rating, Throwable t) {
        return serviceFallback.getReviewsByRatingFallback(rating, t);
    }

    private List<ReviewResponseDTO> handleGetAllReviewsFallback(Throwable t) {
        return serviceFallback.getAllReviewsFallback(t);
    }
}