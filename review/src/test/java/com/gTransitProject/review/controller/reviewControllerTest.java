package com.gTransitProject.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gTransitProject.review.model.reviewModel;
import com.gTransitProject.review.service.reviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(reviewController.class)
class reviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private reviewService service;

    private reviewModel sampleReview;

    @BeforeEach
    void setUp() {
        sampleReview = new reviewModel();
        sampleReview.setId(1);
        sampleReview.setClientId("C-9872");
        sampleReview.setSpecificTrainId("TR-404");
        sampleReview.setSeatNumber("Car-B-42");
        sampleReview.setReviewContent("The commute was very smooth and on time!");
        sampleReview.setRating(5);
        sampleReview.setReviewDate(Date.valueOf("2026-06-26"));
        sampleReview.setRoute("LANY001");
        sampleReview.setTicketId("TKT-112233");
        sampleReview.setDriverInCharge("D-5501");
    }

    @Test
    void getReviews_ShouldReturnCollectionWithEmbeddedReviews() throws Exception {
        List<reviewModel> allReviews = Arrays.asList(sampleReview);
        when(service.getAll()).thenReturn(allReviews);

        mockMvc.perform(get("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reviewModel[0].id").value(1))
                .andExpect(jsonPath("$._embedded.reviewModel[0].clientId").value("C-9872"))
                .andExpect(jsonPath("$._embedded.reviewModel[0].reviewContent").value("The commute was very smooth and on time!"))
                .andExpect(jsonPath("$._embedded.reviewModel[0].rating").value(5))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getReviewById_ShouldReturnSingleReviewWithLinks() throws Exception {
        when(service.getById(1)).thenReturn(sampleReview);

        mockMvc.perform(get("/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ticketId").value("TKT-112233"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.allReviews.href").exists());
    }

    @Test
    void getReviewById_NotFound_ShouldReturn404() throws Exception {
        when(service.getById(999)).thenReturn(null);

        mockMvc.perform(get("/api/reviews/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getReviewByClientId_ShouldReturnSingleReviewWithLinks() throws Exception {
        when(service.getByClientId(9872)).thenReturn(sampleReview);

        mockMvc.perform(get("/api/reviews/client/9872")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value("C-9872"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getReviewByClientId_NotFound_ShouldReturn404() throws Exception {
        when(service.getByClientId(9999)).thenReturn(null);

        mockMvc.perform(get("/api/reviews/client/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getReviewBySpecificTrainId_ShouldReturnSingleReviewWithLinks() throws Exception {
        when(service.getBySpecificTrainId(404)).thenReturn(sampleReview);

        mockMvc.perform(get("/api/reviews/train/404")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.specificTrainId").value("TR-404"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getReviewBySpecificTrainId_NotFound_ShouldReturn404() throws Exception {
        when(service.getBySpecificTrainId(999)).thenReturn(null);

        mockMvc.perform(get("/api/reviews/train/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getReviewByRating_ShouldReturnCollectionWithEmbeddedReviews() throws Exception {
        List<reviewModel> matchingReviews = Arrays.asList(sampleReview);
        when(service.getByRating(5)).thenReturn(matchingReviews);

        mockMvc.perform(get("/api/reviews/rating/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reviewModel[0].id").value(1))
                .andExpect(jsonPath("$._embedded.reviewModel[0].rating").value(5))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void saveReview_ShouldCreateAndReturnReviewWithLinks() throws Exception {
        when(service.createReview(any(reviewModel.class))).thenReturn(sampleReview);

        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleReview)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.driverInCharge").value("D-5501"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void deleteReview_ShouldReturnNoContent() throws Exception {
        doNothing().when(service).deleteReview(1);

        mockMvc.perform(delete("/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}