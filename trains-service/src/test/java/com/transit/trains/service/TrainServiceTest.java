package com.transit.trains.service;

import com.transit.trains.dto.mapper.TrainMapper;
import com.transit.trains.dto.request.TrainRequestDTO;
import com.transit.trains.dto.response.TrainResponseDTO;
import com.transit.trains.fallback.TrainServiceFallback;
import com.transit.trains.model.TrainModel;
import com.transit.trains.repository.TrainsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainServiceTest {

    @Mock
    private TrainsRepository repository;

    @Mock
    private TrainMapper mapper;

    @Mock
    private TrainServiceFallback serviceFallback;

    @InjectMocks
    private TrainService trainService;

    private TrainModel trainModel;
    private TrainResponseDTO responseDTO;
    private TrainRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        trainModel = new TrainModel();
        trainModel.setId(1L);
        trainModel.setCode("T001");

        responseDTO = new TrainResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCode("T001");

        requestDTO = new TrainRequestDTO();
        requestDTO.setCode("T001");
    }

    @Test
    void getTrainById_shouldReturnTrain_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(trainModel));
        when(mapper.toResponse(trainModel)).thenReturn(responseDTO);

        TrainResponseDTO result = trainService.getTrainById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(trainModel);
    }

    @Test
    void getTrainById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainService.getTrainById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Train not found with id: 99");
    }

    @Test
    void getTrainByCode_shouldReturnTrain_whenExists() {
        when(repository.findByCode("T001")).thenReturn(Optional.of(trainModel));
        when(mapper.toResponse(trainModel)).thenReturn(responseDTO);

        TrainResponseDTO result = trainService.getTrainByCode("T001");

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findByCode("T001");
        verify(mapper).toResponse(trainModel);
    }

    @Test
    void getTrainByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainService.getTrainByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Train not found with code: UNKNOWN");
    }

    @Test
    void getTrainsByManufacturerId_shouldReturnListOfTrains() {
        when(repository.findByManufacturerId("Siemens")).thenReturn(List.of(trainModel));
        when(mapper.toResponse(trainModel)).thenReturn(responseDTO);

        List<TrainResponseDTO> result = trainService.getTrainsByManufacturerId("Siemens");

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByManufacturerId("Siemens");
        verify(mapper).toResponse(trainModel);
    }

    @Test
    void getAllTrains_shouldReturnAllTrains() {
        when(repository.findAll()).thenReturn(List.of(trainModel));
        when(mapper.toResponse(trainModel)).thenReturn(responseDTO);

        List<TrainResponseDTO> result = trainService.getAllTrains();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(trainModel);
    }

    @Test
    void getTrainsByEngineId_shouldReturnTrainsForEngine() {
        when(repository.findByEngineId(100L)).thenReturn(List.of(trainModel));
        when(mapper.toResponse(trainModel)).thenReturn(responseDTO);

        List<TrainResponseDTO> result = trainService.getTrainsByEngineId(100L);

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByEngineId(100L);
        verify(mapper).toResponse(trainModel);
    }

    @Test
    void createTrain_shouldSaveAndReturnTrain() {
        when(mapper.toEntity(requestDTO)).thenReturn(trainModel);
        when(repository.save(trainModel)).thenReturn(trainModel);
        when(mapper.toResponse(trainModel)).thenReturn(responseDTO);

        TrainResponseDTO result = trainService.createTrain(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(trainModel);
        verify(mapper).toResponse(trainModel);
    }

    @Test
    void deleteTrain_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        trainService.deleteTrain(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteTrain_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> trainService.deleteTrain(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Train not found with id: 99");
    }

    // ========== FALLBACK TESTS ==========

    @Test
    void handleGetTrainFallback_withId_shouldDelegateToServiceFallback() {
        Throwable t = new RuntimeException("test");
        // The fallback passes the original id (1L), not -1L
        when(serviceFallback.getTrainFallback(1L, t)).thenReturn(responseDTO);

        TrainResponseDTO result = trainService.handleGetTrainFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getTrainFallback(1L, t);
    }

    @Test
    void handleGetTrainFallback_withCode_shouldDelegateToServiceFallbackWithMinusOne() {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getTrainFallback(-1L, t)).thenReturn(responseDTO);

        TrainResponseDTO result = trainService.handleGetTrainFallback("CODE", t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getTrainFallback(-1L, t);
    }

    @Test
    void handleGetTrainFallback_withRequestDTO_shouldDelegateToServiceFallbackWithMinusOne() {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getTrainFallback(-1L, t)).thenReturn(responseDTO);

        TrainResponseDTO result = trainService.handleGetTrainFallback(requestDTO, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getTrainFallback(-1L, t);
    }

    @Test
    void handleGetTrainsFallbackList_withManufacturerId_shouldDelegateToServiceFallback() {
        Throwable t = new RuntimeException("test");
        List<TrainResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getTrainsFallbackList(t)).thenReturn(fallbackList);

        List<TrainResponseDTO> result = trainService.handleGetTrainsFallbackList("Siemens", t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getTrainsFallbackList(t);
    }

    @Test
    void handleGetTrainsFallbackList_withoutParams_shouldDelegateToServiceFallback() {
        Throwable t = new RuntimeException("test");
        List<TrainResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getTrainsFallbackList(t)).thenReturn(fallbackList);

        List<TrainResponseDTO> result = trainService.handleGetTrainsFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getTrainsFallbackList(t);
    }
}