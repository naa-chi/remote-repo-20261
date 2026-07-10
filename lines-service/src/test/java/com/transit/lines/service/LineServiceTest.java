package com.transit.lines.service;

import com.transit.lines.dto.mapper.LineMapper;
import com.transit.lines.dto.request.LineRequestDTO;
import com.transit.lines.dto.response.LineResponseDTO;
import com.transit.lines.fallback.LineServiceFallback;
import com.transit.lines.model.LineModel;
import com.transit.lines.repository.LineRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository repository;

    @Mock
    private LineMapper mapper;

    @Mock
    private LineServiceFallback fallback;

    @InjectMocks
    private LineService lineService;

    private LineModel model;
    private LineResponseDTO responseDTO;
    private LineRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        model = new LineModel();
        model.setId(1L);
        model.setLineCode(10);
        model.setLineLengthKM(42L);
        model.setPeopleServedMonthlyEstimate(500000L);

        responseDTO = new LineResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setLineCode(10);
        responseDTO.setLineLengthKM(42L);
        responseDTO.setPeopleServedMonthlyEstimate(500000L);

        requestDTO = new LineRequestDTO();
        requestDTO.setLineCode(10);
        requestDTO.setLineLengthKM(42L);
        requestDTO.setPeopleServedMonthlyEstimate(500000L);
    }

    // ---- getLineById ----

    @Test
    void getLineById_shouldReturnLine_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        LineResponseDTO result = lineService.getLineById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getLineById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lineService.getLineById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Line not found with id: 99");
    }

    // ---- getLineByCode ----

    @Test
    void getLineByCode_shouldReturnLine_whenExists() {
        when(repository.findByLineCode(10)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        LineResponseDTO result = lineService.getLineByCode(10);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findByLineCode(10);
        verify(mapper).toResponse(model);
    }

    @Test
    void getLineByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findByLineCode(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lineService.getLineByCode(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Line not found with code: 999");
    }

    // ---- createLine ----

    @Test
    void createLine_shouldSaveAndReturn() {
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        LineResponseDTO result = lineService.createLine(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(model);
        verify(mapper).toResponse(model);
    }

    // ---- updateLine ----

    @Test
    void updateLine_shouldUpdateAndReturn_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(any(LineModel.class))).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        LineResponseDTO result = lineService.updateLine(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(any(LineModel.class));
    }

    @Test
    void updateLine_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lineService.updateLine(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Line not found with id: 99");
    }

    // ---- deleteLine ----

    @Test
    void deleteLine_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        lineService.deleteLine(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteLine_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> lineService.deleteLine(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Line not found with id: 99");
    }

    // ---- getAllLines ----

    @Test
    void getAllLines_shouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<LineResponseDTO> result = lineService.getAllLines();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    // ========== FALLBACK TESTS ==========
    // Fallback methods are public, so we can call them directly.

    @Test
    void handleGetLineFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getLineFallback(1L, t)).thenReturn(responseDTO);

        LineResponseDTO result = lineService.handleGetLineFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getLineFallback(1L, t);
    }

    @Test
    void handleGetLineByCodeFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getLineFallback(10, t)).thenReturn(responseDTO);

        LineResponseDTO result = lineService.handleGetLineByCodeFallback(10, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getLineFallback(10, t);
    }

    @Test
    void handleGetLinesFallbackList_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<LineResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getLinesFallbackList(t)).thenReturn(fallbackList);

        List<LineResponseDTO> result = lineService.handleGetLinesFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getLinesFallbackList(t);
    }
}