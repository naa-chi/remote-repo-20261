package com.transit.reviews.service;

import com.transit.reviews.client.TicketClient;
import com.transit.reviews.client.TrainClient;
import com.transit.reviews.dto.mapper.ReviewMapper;
import com.transit.reviews.dto.request.ReviewRequestDTO;
import com.transit.reviews.dto.response.ReviewResponseDTO;
import com.transit.reviews.fallback.ReviewServiceFallback;
import com.transit.reviews.model.ReviewModel;
import com.transit.reviews.repository.ReviewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewsRepository repository;

    @Mock
    private ReviewMapper mapper;

    @Mock
    private ReviewServiceFallback serviceFallback;

    @Mock
    private TicketClient ticketClient;

    @Mock
    private TrainClient trainClient;

    @InjectMocks
    private ReviewService reviewService;

    private ReviewModel model;
    private ReviewResponseDTO responseDTO;
    private ReviewRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date reviewDate = Date.valueOf("2024-01-01");

        model = new ReviewModel();
        model.setId(1L);
        model.setClientId(100L);
        model.setTrainId(200L);
        model.setTicketCode("TICKET001");
        model.setRating(5);
        model.setComment("Great service!");
        model.setReviewDate(reviewDate);

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

    // ---- getReviewById ----

    @Test
    void getReviewById_shouldReturnReview_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ReviewResponseDTO result = reviewService.getReviewById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getReviewById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.getReviewById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Review not found with id: 99");
    }

    // ---- getReviewsByClientId ----

    @Test
    void getReviewsByClientId_shouldReturnList_whenExists() {
        when(repository.findByClientId(100L)).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<ReviewResponseDTO> result = reviewService.getReviewsByClientId(100L);

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByClientId(100L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getReviewsByClientId_shouldThrowRuntimeException_whenEmpty() {
        when(repository.findByClientId(99L)).thenReturn(List.of());

        assertThatThrownBy(() -> reviewService.getReviewsByClientId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No reviews found for client ID: 99");
    }

    // ---- getReviewsByTrainId ----

    @Test
    void getReviewsByTrainId_shouldReturnList_whenExists() {
        when(repository.findByTrainId(200L)).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<ReviewResponseDTO> result = reviewService.getReviewsByTrainId(200L);

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByTrainId(200L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getReviewsByTrainId_shouldThrowRuntimeException_whenEmpty() {
        when(repository.findByTrainId(99L)).thenReturn(List.of());

        assertThatThrownBy(() -> reviewService.getReviewsByTrainId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No reviews found for train ID: 99");
    }

    // ---- getReviewsByRating ----

    @Test
    void getReviewsByRating_shouldReturnList_whenExists() {
        when(repository.findByRating(5)).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<ReviewResponseDTO> result = reviewService.getReviewsByRating(5);

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByRating(5);
        verify(mapper).toResponse(model);
    }

    @Test
    void getReviewsByRating_shouldThrowRuntimeException_whenEmpty() {
        when(repository.findByRating(1)).thenReturn(List.of());

        assertThatThrownBy(() -> reviewService.getReviewsByRating(1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No reviews found with rating: 1");
    }

    // ---- getAllReviews ----

    @Test
    void getAllReviews_shouldReturnList_whenExists() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<ReviewResponseDTO> result = reviewService.getAllReviews();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    @Test
    void getAllReviews_shouldThrowRuntimeException_whenEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> reviewService.getAllReviews())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No reviews found in the database.");
    }

    // ---- createReview ----

    @Test
    void createReview_shouldSaveAndReturn_whenTicketAndTrainExist() {
        // Stub Feign clients to return null – the service only cares about exceptions
        when(ticketClient.getTicketByCode(anyString())).thenReturn(null);
        when(trainClient.getTrainById(anyLong())).thenReturn(null);

        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ReviewResponseDTO result = reviewService.createReview(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(ticketClient).getTicketByCode("TICKET001");
        verify(trainClient).getTrainById(200L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(model);
        verify(mapper).toResponse(model);
    }

    @Test
    void createReview_shouldThrowRuntimeException_whenTicketNotFound() {
        doThrow(new RuntimeException("Ticket not found")).when(ticketClient).getTicketByCode("TICKET001");

        assertThatThrownBy(() -> reviewService.createReview(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket not found with code: TICKET001");

        verify(ticketClient).getTicketByCode("TICKET001");
        verify(trainClient, never()).getTrainById(anyLong());
        verify(repository, never()).save(any());
    }

    @Test
    void createReview_shouldThrowRuntimeException_whenTrainNotFound() {
        when(ticketClient.getTicketByCode(anyString())).thenReturn(null);
        doThrow(new RuntimeException("Train not found")).when(trainClient).getTrainById(200L);

        assertThatThrownBy(() -> reviewService.createReview(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Train not found with ID: 200");

        verify(ticketClient).getTicketByCode("TICKET001");
        verify(trainClient).getTrainById(200L);
        verify(repository, never()).save(any());
    }

    // ---- deleteReview ----

    @Test
    void deleteReview_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        reviewService.deleteReview(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteReview_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> reviewService.deleteReview(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Review not found with id: 99");
    }

    // ========== FALLBACK TESTS (using reflection) ==========

    @Test
    void handleGetReviewFallback_shouldDelegateToFallback() throws Exception {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getReviewFallback(1L, t)).thenReturn(responseDTO);

        Method method = ReviewService.class.getDeclaredMethod("handleGetReviewFallback", Long.class, Throwable.class);
        method.setAccessible(true);
        ReviewResponseDTO result = (ReviewResponseDTO) method.invoke(reviewService, 1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getReviewFallback(1L, t);
    }

    @Test
    void handleGetReviewsByClientIdFallback_shouldDelegateToFallback() throws Exception {
        Throwable t = new RuntimeException("test");
        List<ReviewResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getReviewsByClientIdFallback(100L, t)).thenReturn(fallbackList);

        Method method = ReviewService.class.getDeclaredMethod("handleGetReviewsByClientIdFallback", Long.class, Throwable.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<ReviewResponseDTO> result = (List<ReviewResponseDTO>) method.invoke(reviewService, 100L, t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getReviewsByClientIdFallback(100L, t);
    }

    @Test
    void handleGetReviewsByTrainIdFallback_shouldDelegateToFallback() throws Exception {
        Throwable t = new RuntimeException("test");
        List<ReviewResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getReviewsByTrainIdFallback(200L, t)).thenReturn(fallbackList);

        Method method = ReviewService.class.getDeclaredMethod("handleGetReviewsByTrainIdFallback", Long.class, Throwable.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<ReviewResponseDTO> result = (List<ReviewResponseDTO>) method.invoke(reviewService, 200L, t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getReviewsByTrainIdFallback(200L, t);
    }

    @Test
    void handleGetReviewsByRatingFallback_shouldDelegateToFallback() throws Exception {
        Throwable t = new RuntimeException("test");
        List<ReviewResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getReviewsByRatingFallback(5, t)).thenReturn(fallbackList);

        Method method = ReviewService.class.getDeclaredMethod("handleGetReviewsByRatingFallback", Integer.class, Throwable.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<ReviewResponseDTO> result = (List<ReviewResponseDTO>) method.invoke(reviewService, 5, t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getReviewsByRatingFallback(5, t);
    }

    @Test
    void handleGetAllReviewsFallback_shouldDelegateToFallback() throws Exception {
        Throwable t = new RuntimeException("test");
        List<ReviewResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getAllReviewsFallback(t)).thenReturn(fallbackList);

        Method method = ReviewService.class.getDeclaredMethod("handleGetAllReviewsFallback", Throwable.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<ReviewResponseDTO> result = (List<ReviewResponseDTO>) method.invoke(reviewService, t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getAllReviewsFallback(t);
    }

    @Test
    void handleCreateReviewFallback_shouldDelegateToFallback() throws Exception {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getReviewFallback(-1L, t)).thenReturn(responseDTO);

        Method method = ReviewService.class.getDeclaredMethod("handleCreateReviewFallback", ReviewRequestDTO.class, Throwable.class);
        method.setAccessible(true);
        ReviewResponseDTO result = (ReviewResponseDTO) method.invoke(reviewService, requestDTO, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getReviewFallback(-1L, t);
    }
}