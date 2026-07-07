package com.transit.reviews.repository;

import com.transit.reviews.model.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewModel, Long> {
    List<ReviewModel> findByClientId(Long clientId);
    List<ReviewModel> findByTrainId(Long trainId);
    List<ReviewModel> findByRating(Integer rating);
}