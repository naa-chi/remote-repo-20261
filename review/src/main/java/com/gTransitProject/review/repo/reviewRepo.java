package com.gTransitProject.review.repo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gTransitProject.review.model.reviewModel;

@Repository
public interface reviewRepo extends JpaRepository<reviewModel, Integer> {
    Optional<reviewModel> findByClientId(Integer clientId);
    List<reviewModel> findByRating(Integer rating); //idk man

}
