package com.transit.manufacturers.service;

import com.transit.manufacturers.dto.mapper.ManufacturerMapper;
import com.transit.manufacturers.dto.request.ManufacturerRequestDTO;
import com.transit.manufacturers.dto.response.ManufacturerResponseDTO;
import com.transit.manufacturers.fallback.ManufacturerServiceFallback;
import com.transit.manufacturers.model.ManufacturerModel;
import com.transit.manufacturers.repository.ManufacturerRepository;
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
class ManufacturerServiceTest {

    @Mock
    private ManufacturerRepository repository;

    @Mock
    private ManufacturerMapper mapper;

    @Mock
    private ManufacturerServiceFallback fallback;

    @InjectMocks
    private ManufacturerService manufacturerService;

    private ManufacturerModel model;
    private ManufacturerResponseDTO responseDTO;
    private ManufacturerRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date foundingDate = Date.valueOf("2020-01-01");

        model = new ManufacturerModel();
        model.setId(1L);
        model.setManufacturerId("SIEMENS");
        model.setCountryOfOrigin("Germany");
        model.setFoundingDate(foundingDate);
        model.setRevenue(1000000L);

        responseDTO = new ManufacturerResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setManufacturerId("SIEMENS");
        responseDTO.setCountryOfOrigin("Germany");
        responseDTO.setFoundingDate(foundingDate);
        responseDTO.setRevenue(1000000L);

        requestDTO = new ManufacturerRequestDTO();
        requestDTO.setManufacturerId("SIEMENS");
        requestDTO.setCountryOfOrigin("Germany");
        requestDTO.setFoundingDate(foundingDate);
        requestDTO.setRevenue(1000000L);
    }

    // ---- getManufacturerById ----

    @Test
    void getManufacturerById_shouldReturnManufacturer_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ManufacturerResponseDTO result = manufacturerService.getManufacturerById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getManufacturerById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manufacturerService.getManufacturerById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Manufacturer not found with id: 99");
    }

    // ---- getManufacturerByCode ----

    @Test
    void getManufacturerByCode_shouldReturnManufacturer_whenExists() {
        when(repository.findByManufacturerId("SIEMENS")).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ManufacturerResponseDTO result = manufacturerService.getManufacturerByCode("SIEMENS");

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findByManufacturerId("SIEMENS");
        verify(mapper).toResponse(model);
    }

    @Test
    void getManufacturerByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findByManufacturerId("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manufacturerService.getManufacturerByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Manufacturer not found with code: UNKNOWN");
    }

    // ---- createManufacturer ----

    @Test
    void createManufacturer_shouldSaveAndReturn() {
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ManufacturerResponseDTO result = manufacturerService.createManufacturer(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(model);
        verify(mapper).toResponse(model);
    }

    // ---- updateManufacturer ----

    @Test
    void updateManufacturer_shouldUpdateAndReturn_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(any(ManufacturerModel.class))).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ManufacturerResponseDTO result = manufacturerService.updateManufacturer(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(any(ManufacturerModel.class));
    }

    @Test
    void updateManufacturer_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manufacturerService.updateManufacturer(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Manufacturer not found with id: 99");
    }

    // ---- deleteManufacturer ----

    @Test
    void deleteManufacturer_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        manufacturerService.deleteManufacturer(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteManufacturer_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> manufacturerService.deleteManufacturer(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Manufacturer not found with id: 99");
    }

    // ---- getAllManufacturers ----

    @Test
    void getAllManufacturers_shouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<ManufacturerResponseDTO> result = manufacturerService.getAllManufacturers();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    // ========== FALLBACK TESTS ==========

    @Test
    void handleGetManufacturerFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getManufacturerFallback(1L, t)).thenReturn(responseDTO);

        ManufacturerResponseDTO result = manufacturerService.handleGetManufacturerFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getManufacturerFallback(1L, t);
    }

    @Test
    void handleGetManufacturerByCodeFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getManufacturerFallback("CODE", t)).thenReturn(responseDTO);

        ManufacturerResponseDTO result = manufacturerService.handleGetManufacturerByCodeFallback("CODE", t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getManufacturerFallback("CODE", t);
    }

    @Test
    void handleGetManufacturersFallbackList_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<ManufacturerResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getManufacturersFallbackList(t)).thenReturn(fallbackList);

        List<ManufacturerResponseDTO> result = manufacturerService.handleGetManufacturersFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getManufacturersFallbackList(t);
    }
}