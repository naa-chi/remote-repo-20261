package com.transit.reviews.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.reviews.dto.request.ReviewRequestDTO;
import com.transit.reviews.dto.response.ReviewResponseDTO;
import com.transit.reviews.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReviewService reviewService;

    private ReviewResponseDTO responseDTO;
    private ReviewRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date reviewDate = Date.valueOf("2024-01-01");

        responseDTO = new ReviewResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setClientId(100L);
        responseDTO.setTrainId(200L);
        responseDTO.setTicketCode("TICKET001");
        responseDTO.setRating(5);
        responseDTO.setComment("Great service!");
        responseDTO.setReviewDate(reviewDate);

        requestDTO = new ReviewRequestDTO();
        requestDTO.setClientId(100L);
        requestDTO.setTrainId(200L);
        requestDTO.setTicketCode("TICKET001");
        requestDTO.setRating(5);
        requestDTO.setComment("Great service!");
        requestDTO.setReviewDate(reviewDate);
    }

    // ---- GET /api/reviews ----

    @Test
    void getAllReviews_shouldReturnList() throws Exception {
        when(reviewService.getAllReviews()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/reviews")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0].clientId", is(100)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0]._links.self.href", containsString("/api/reviews/1")))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0]._links.all-reviews.href", containsString("/api/reviews")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/reviews")));

        verify(reviewService).getAllReviews();
    }

    // ---- GET /api/reviews/{id} ----

    @Test
    void getReviewById_shouldReturnReview() throws Exception {
        when(reviewService.getReviewById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/reviews/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.clientId", is(100)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/reviews/1")))
                .andExpect(jsonPath("$._links.all-reviews.href", containsString("/api/reviews")));

        verify(reviewService).getReviewById(1L);
    }

    // ---- GET /api/reviews/client/{clientId} ----

    @Test
    void getReviewsByClientId_shouldReturnList() throws Exception {
        when(reviewService.getReviewsByClientId(100L)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/reviews/client/100")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0].clientId", is(100)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0]._links.self.href", containsString("/api/reviews/1")))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0]._links.all-reviews.href", containsString("/api/reviews")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/reviews/client/100")))
                .andExpect(jsonPath("$._links.all-reviews.href", containsString("/api/reviews")));

        verify(reviewService).getReviewsByClientId(100L);
    }

    // ---- GET /api/reviews/train/{trainId} ----

    @Test
    void getReviewsByTrainId_shouldReturnList() throws Exception {
        when(reviewService.getReviewsByTrainId(200L)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/reviews/train/200")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0].trainId", is(200)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0]._links.self.href", containsString("/api/reviews/1")))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0]._links.all-reviews.href", containsString("/api/reviews")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/reviews/train/200")))
                .andExpect(jsonPath("$._links.all-reviews.href", containsString("/api/reviews")));

        verify(reviewService).getReviewsByTrainId(200L);
    }

    // ---- GET /api/reviews/rating/{rating} ----

    @Test
    void getReviewsByRating_shouldReturnList() throws Exception {
        when(reviewService.getReviewsByRating(5)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/reviews/rating/5")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0].rating", is(5)))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0]._links.self.href", containsString("/api/reviews/1")))
                .andExpect(jsonPath("$._embedded.reviewResponseDTOList[0]._links.all-reviews.href", containsString("/api/reviews")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/reviews/rating/5")))
                .andExpect(jsonPath("$._links.all-reviews.href", containsString("/api/reviews")));

        verify(reviewService).getReviewsByRating(5);
    }

    // ---- POST /api/reviews ----

    @Test
    void createReview_shouldReturnCreated() throws Exception {
        when(reviewService.createReview(any(ReviewRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.clientId", is(100)))
                .andExpect(jsonPath("$.ticketCode", is("TICKET001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/reviews/1")))
                .andExpect(jsonPath("$._links.all-reviews.href", containsString("/api/reviews")));

        verify(reviewService).createReview(any(ReviewRequestDTO.class));
    }

    // ---- DELETE /api/reviews/{id} ----

    @Test
    void deleteReview_shouldReturnNoContent() throws Exception {
        doNothing().when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());

        verify(reviewService).deleteReview(1L);
    }
}