package com.transit.engines.service;

import com.transit.engines.dto.mapper.EngineAssembler;
import com.transit.engines.dto.request.EngineRequestDTO;
import com.transit.engines.dto.response.EngineResponseDTO;
import com.transit.engines.fallback.EngineServiceFallback;
import com.transit.engines.model.EngineModel;
import com.transit.engines.repository.EnginesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EngineServiceTest {

    @Mock
    private EnginesRepository repository;

    @Mock
    private EngineAssembler mapper;

    @Mock
    private EngineServiceFallback serviceFallback;

    @InjectMocks
    private EngineService engineService;

    private EngineModel model;
    private EngineResponseDTO responseDTO;
    private EngineRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date productionDate = Date.valueOf("2024-01-01");

        model = new EngineModel();
        model.setId(1L);
        model.setEngineId(100L);
        model.setManufacturerId("SIEMENS");
        model.setEngineCode("E001");
        model.setEngineHorsepower(4400.0f);
        model.setEngineWeight(120.5f);
        model.setEnginePrice(150000.0f);
        model.setProductionDate(productionDate);

        responseDTO = new EngineResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEngineId(100L);
        responseDTO.setManufacturerId("SIEMENS");
        responseDTO.setEngineCode("E001");
        responseDTO.setEngineHorsepower(4400.0f);
        responseDTO.setEngineWeight(120.5f);
        responseDTO.setEnginePrice(150000.0f);
        responseDTO.setProductionDate(productionDate);

        requestDTO = new EngineRequestDTO();
        requestDTO.setEngineId(100L);
        requestDTO.setManufacturerId("SIEMENS");
        requestDTO.setEngineCode("E001");
        requestDTO.setEngineHorsepower(4400.0f);
        requestDTO.setEngineWeight(120.5f);
        requestDTO.setEnginePrice(150000.0f);
        requestDTO.setProductionDate(productionDate);
    }

    // ---- getEngineById ----

    @Test
    void getEngineById_shouldReturnEngine_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        EngineResponseDTO result = engineService.getEngineById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getEngineById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> engineService.getEngineById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Engine not found with id: 99");
    }

    // ---- createEngine ----

    @Test
    void createEngine_shouldSaveAndReturn() {
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        EngineResponseDTO result = engineService.createEngine(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(model);
        verify(mapper).toResponse(model);
    }

    // ---- updateEngine ----

    @Test
    void updateEngine_shouldUpdateAndReturn_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(any(EngineModel.class))).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        EngineResponseDTO result = engineService.updateEngine(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(any(EngineModel.class));
    }

    @Test
    void updateEngine_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> engineService.updateEngine(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Engine not found with id: 99");
    }

    // ---- deleteEngine ----

    @Test
    void deleteEngine_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        engineService.deleteEngine(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteEngine_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> engineService.deleteEngine(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Engine not found with id: 99");
    }

    // ---- getEnginesByManufacturerId ----

    @Test
    void getEnginesByManufacturerId_shouldReturnList() {
        when(repository.findByManufacturerId("SIEMENS")).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<EngineResponseDTO> result = engineService.getEnginesByManufacturerId("SIEMENS");

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByManufacturerId("SIEMENS");
        verify(mapper).toResponse(model);
    }

    // ---- getEnginesByEngineCode ----

    @Test
    void getEnginesByEngineCode_shouldReturnList() {
        when(repository.findByEngineCode("E001")).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<EngineResponseDTO> result = engineService.getEnginesByEngineCode("E001");

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByEngineCode("E001");
        verify(mapper).toResponse(model);
    }

    // ---- getEngineHorsepower ----

    @Test
    void getEngineHorsepower_shouldReturnList() {
        when(repository.findByEngineHorsepower(4400.0f)).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<EngineResponseDTO> result = engineService.getEngineHorsepower(4400.0f);

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByEngineHorsepower(4400.0f);
        verify(mapper).toResponse(model);
    }

    // ---- getEnginePrice ----

    @Test
    void getEnginePrice_shouldReturnList() {
        when(repository.findByEnginePrice(150000.0f)).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<EngineResponseDTO> result = engineService.getEnginePrice(150000.0f);

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByEnginePrice(150000.0f);
        verify(mapper).toResponse(model);
    }

    // ---- getEngineWeight ----

    @Test
    void getEngineWeight_shouldReturnList() {
        when(repository.findByEngineWeight(120.5f)).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<EngineResponseDTO> result = engineService.getEngineWeight(120.5f);

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByEngineWeight(120.5f);
        verify(mapper).toResponse(model);
    }

    // ---- getAllEngines ----

    @Test
    void getAllEngines_shouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<EngineResponseDTO> result = engineService.getAllEngines();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    // ========== FALLBACK TESTS ==========
    // Fallback methods are public, so we can call them directly.

    @Test
    void handleGetEngineFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getEngineFallback(1L, t)).thenReturn(responseDTO);

        EngineResponseDTO result = engineService.handleGetEngineFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getEngineFallback(1L, t);
    }

    @Test
    void handleGetEngineFallback_withRequest_shouldDelegateToFallbackWithMinusOne() {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getEngineFallback(-1L, t)).thenReturn(responseDTO);

        EngineResponseDTO result = engineService.handleGetEngineFallback(requestDTO, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getEngineFallback(-1L, t);
    }

    @Test
    void handleGetEngineFallback_withIdAndRequest_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getEngineFallback(1L, t)).thenReturn(responseDTO);

        EngineResponseDTO result = engineService.handleGetEngineFallback(1L, requestDTO, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getEngineFallback(1L, t);
    }

    @Test
    void handleGetEnginesFallbackString_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<EngineResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getEnginesFallbackList(t)).thenReturn(fallbackList);

        List<EngineResponseDTO> result = engineService.handleGetEnginesFallbackString("SIEMENS", t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getEnginesFallbackList(t);
    }

    @Test
    void handleGetEnginesFallbackFloat_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<EngineResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getEnginesFallbackList(t)).thenReturn(fallbackList);

        List<EngineResponseDTO> result = engineService.handleGetEnginesFallbackFloat(4400.0f, t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getEnginesFallbackList(t);
    }

    @Test
    void handleGetEnginesFallbackNoParam_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<EngineResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getEnginesFallbackList(t)).thenReturn(fallbackList);

        List<EngineResponseDTO> result = engineService.handleGetEnginesFallbackNoParam(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getEnginesFallbackList(t);
    }
}