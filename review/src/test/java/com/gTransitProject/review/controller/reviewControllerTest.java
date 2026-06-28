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
    private ObjectMapper objectMapper; // Converts objects to JSON string payloads

    @MockitoBean
    private reviewService service;

    private reviewModel sampleReview;

    @BeforeEach
    void setUp() {
        // Build a complete sample entity matching your reviewModel definitions
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
    void getReviews_ShouldReturnAllRecords() throws Exception {
        List<reviewModel> allReviews = Arrays.asList(sampleReview);
        when(service.getAll()).thenReturn(allReviews);

        mockMvc.perform(get("/reviews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clientId").value("C-9872"))
                .andExpect(jsonPath("$[0].reviewContent").value("The commute was very smooth and on time!"))
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    void getReviewById_ShouldReturnSingleReview() throws Exception {
        when(service.getById(1)).thenReturn(sampleReview);

        mockMvc.perform(get("/reviews/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ticketId").value("TKT-112233"));
    }

    @Test
    void getReviewByClientId_ShouldReturnMatchingReview() throws Exception {
        // Mapping paths to handle integers as specified by your @PathVariable inside the controller
        when(service.getByClientId(9872)).thenReturn(sampleReview);

        mockMvc.perform(get("/reviews/client/9872")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value("C-9872"));
    }

    @Test
    void getReviewBySpecificTrainId_ShouldReturnMatchingReview() throws Exception {
        when(service.getBySpecificTrainId(404)).thenReturn(sampleReview);

        mockMvc.perform(get("/reviews/train/404")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.specificTrainId").value("TR-404"));
    }

    @Test
    void getReviewByRating_ShouldReturnListOfReviewsWithSameRating() throws Exception {
        List<reviewModel> matchingReviews = Arrays.asList(sampleReview);
        when(service.getByRating(5)).thenReturn(matchingReviews);

        mockMvc.perform(get("/reviews/rating/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    void saveReview_ShouldCreateAndReturnReview() throws Exception {
        when(service.createReview(any(reviewModel.class))).thenReturn(sampleReview);

        mockMvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleReview)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.driverInCharge").value("D-5501"));
    }

    @Test
    void deleteReview_ShouldExecuteSuccessfully() throws Exception {
        doNothing().when(service).deleteReview(1);

        mockMvc.perform(delete("/reviews/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Resolves void with standard HTTP 200 OK
    }
}