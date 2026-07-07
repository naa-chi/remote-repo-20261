package com.transit.reviews.assembler;

import com.transit.reviews.dto.request.ReviewRequestDTO;
import com.transit.reviews.dto.response.ReviewResponseDTO;
import com.transit.reviews.model.ReviewModel;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponseDTO toResponse(ReviewModel model) {
        if (model == null) {
            return null;
        }

        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(model.getId());
        response.setClientId(model.getClientId());
        response.setTrainId(model.getTrainId());
        response.setRating(model.getRating());
        response.setComment(model.getComment());
        response.setReviewDate(model.getReviewDate());

        return response;
    }

    public ReviewModel toEntity(ReviewRequestDTO request) {
        if (request == null) {
            return null;
        }

        ReviewModel model = new ReviewModel();
        model.setClientId(request.getClientId());
        model.setTrainId(request.getTrainId());
        model.setRating(request.getRating());
        model.setComment(request.getComment());
        model.setReviewDate(request.getReviewDate());

        return model;
    }
}