package com.gTransitProject.review.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

import com.gTransitProject.review.model.reviewModel;
import com.gTransitProject.review.repo.reviewRepo;

@Service
@Slf4j
@RequiredArgsConstructor
public class reviewService {
    private final reviewRepo repo;

    public List<reviewModel> getAll() {
        log.info("Fetching all reviews");
        return repo.findAll();
    }

    public reviewModel getById(Integer id) {
        log.info("Fetching review with id: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to find review with id: {}", id);
                    return new RuntimeException("Review not found with id: " + id);
                });
    }

    public reviewModel getByClientId(String clientId) {
        log.info("Fetching review by client id: {}", clientId);
        return repo.findByClientId(clientId)
                .orElseThrow(() -> {
                    log.error("Failed to find review with client id: {}", clientId);
                    return new RuntimeException("Review not found with client id: " + clientId);
                });
    }

    public List<reviewModel> getByRating(Integer rating) {
        log.info("Fetching reviews by rating: {}", rating);
        return repo.findByRating(rating);
    }

    public List<reviewModel> getByRoute(String route) {
        log.info("Fetching reviews by route: {}", route);
        return repo.findAll().stream()
                .filter(review -> review.getRoute().equals(route))
                .toList();
    }

    public List<reviewModel> getByDate(String date) {
        log.info("Fetching reviews by date: {}", date);
        return repo.findAll().stream()
                .filter(review -> review.getReviewDate().equals(date))
                .toList();
    }

    public reviewModel getBySpecificTrainId (Integer trainId) {
        log.info("Fetching review by specific train id: {}", trainId);
        return repo.findAll().stream()
                .filter(review -> review.getSpecificTrainId().equals(trainId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Failed to find review with specific train id: {}", trainId);
                    return new RuntimeException("Review not found with specific train id: " + trainId);
                });
    }

    public reviewModel createReview(reviewModel reviewData) {
        log.info("Creating a new review");
        return repo.save(reviewData);
    }

    public void deleteReview(Integer id) {
        log.info("Deleting review with id: {}", id);
        repo.deleteById(id);
    }

    public reviewModel updateReview(Integer id, reviewModel updatedReview) {
        log.info("Updating review with id: {}", id);
        return repo.findById(id)
                .map(existing -> {
                    existing.setClientId(updatedReview.getClientId());
                    existing.setRating(updatedReview.getRating());
                    return repo.save(existing);
                })
                .orElseThrow(() -> {
                    log.error("Failed to find review with id: {}", id);
                    return new RuntimeException("Review not found with id: " + id);
                });
    }
}
