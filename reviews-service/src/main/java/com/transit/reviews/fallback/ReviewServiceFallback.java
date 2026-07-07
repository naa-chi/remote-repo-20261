package com.transit.reviews.fallback;

import com.transit.reviews.dto.response.ReviewResponseDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class ReviewServiceFallback {

    public ReviewResponseDTO getReviewFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for Review ID " + id + ". Error: " + t.getMessage());
        return createDefaultFallbackReview(id != null ? id : -1L);
    }

    public List<ReviewResponseDTO> getReviewsByClientIdFallback(Long clientId, Throwable t) {
        System.err.println("CircuitBreaker triggered for Client ID " + clientId + ". Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackReview(-1L));
    }

    public List<ReviewResponseDTO> getReviewsByTrainIdFallback(Long trainId, Throwable t) {
        System.err.println("CircuitBreaker triggered for Train ID " + trainId + ". Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackReview(-1L));
    }

    public List<ReviewResponseDTO> getReviewsByRatingFallback(Integer rating, Throwable t) {
        System.err.println("CircuitBreaker triggered for Rating " + rating + ". Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackReview(-1L));
    }

    public List<ReviewResponseDTO> getAllReviewsFallback(Throwable t) {
        System.err.println("CircuitBreaker triggered for get all reviews. Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackReview(-1L));
    }

    private ReviewResponseDTO createDefaultFallbackReview(Long id) {
        ReviewResponseDTO fallbackDto = new ReviewResponseDTO();
        fallbackDto.setId(id);
        fallbackDto.setClientId(0L);
        fallbackDto.setTrainId(0L);
        fallbackDto.setRating(0);
        fallbackDto.setComment("ERROR");
        fallbackDto.setReviewDate(Date.valueOf(LocalDate.now()));
        return fallbackDto;
    }
}