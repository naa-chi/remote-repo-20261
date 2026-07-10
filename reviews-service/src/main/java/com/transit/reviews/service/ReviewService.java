package com.transit.reviews.service;

import com.transit.reviews.client.TicketClient;
import com.transit.reviews.client.TrainClient;
import com.transit.reviews.dto.mapper.ReviewMapper;
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
    private final TicketClient ticketClient;
    private final TrainClient trainClient; // optional

    public ReviewService(ReviewsRepository repository,
                         ReviewMapper mapper,
                         ReviewServiceFallback serviceFallback,
                         TicketClient ticketClient,
                         TrainClient trainClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceFallback = serviceFallback;
        this.ticketClient = ticketClient;
        this.trainClient = trainClient;
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

    @CircuitBreaker(name = "reviewService", fallbackMethod = "handleCreateReviewFallback")
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        // 1. Validate ticket exists
        try {
            ticketClient.getTicketByCode(request.getTicketCode());
        } catch (Exception e) {
            throw new RuntimeException("Ticket not found with code: " + request.getTicketCode());
        }

        // 2. (Optional) Validate train exists
        try {
            trainClient.getTrainById(request.getTrainId());
        } catch (Exception e) {
            throw new RuntimeException("Train not found with ID: " + request.getTrainId());
        }

        ReviewModel model = mapper.toEntity(request);
        return mapper.toResponse(repository.save(model));
    }

    @CircuitBreaker(name = "reviewService", fallbackMethod = "handleGetReviewFallback")
    public void deleteReview(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Review not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // --- Fallbacks ---
    public ReviewResponseDTO handleGetReviewFallback(Long id, Throwable t) {
        return serviceFallback.getReviewFallback(id, t);
    }

    public List<ReviewResponseDTO> handleGetReviewsByClientIdFallback(Long clientId, Throwable t) {
        return serviceFallback.getReviewsByClientIdFallback(clientId, t);
    }

    public List<ReviewResponseDTO> handleGetReviewsByTrainIdFallback(Long trainId, Throwable t) {
        return serviceFallback.getReviewsByTrainIdFallback(trainId, t);
    }

    public List<ReviewResponseDTO> handleGetReviewsByRatingFallback(Integer rating, Throwable t) {
        return serviceFallback.getReviewsByRatingFallback(rating, t);
    }

    public List<ReviewResponseDTO> handleGetAllReviewsFallback(Throwable t) {
        return serviceFallback.getAllReviewsFallback(t);
    }

    public ReviewResponseDTO handleCreateReviewFallback(ReviewRequestDTO request, Throwable t) {
        return serviceFallback.getReviewFallback(-1L, t);
    }
}