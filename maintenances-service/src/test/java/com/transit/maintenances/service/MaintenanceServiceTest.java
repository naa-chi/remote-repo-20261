package com.transit.maintenances.service;

import com.transit.maintenances.dto.mapper.MaintenanceMapper;
import com.transit.maintenances.dto.request.MaintenanceRequestDTO;
import com.transit.maintenances.dto.response.MaintenanceResponseDTO;
import com.transit.maintenances.fallback.MaintenanceServiceFallback;
import com.transit.maintenances.model.MaintenanceModel;
import com.transit.maintenances.repository.MaintenanceRepository;
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
class MaintenanceServiceTest {

    @Mock
    private MaintenanceRepository repository;

    @Mock
    private MaintenanceMapper mapper;

    @Mock
    private MaintenanceServiceFallback serviceFallback;

    @InjectMocks
    private MaintenanceService maintenanceService;

    private MaintenanceModel model;
    private MaintenanceResponseDTO responseDTO;
    private MaintenanceRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date entryDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-01-05");
        Date releaseDate = Date.valueOf("2024-01-10");

        model = new MaintenanceModel();
        model.setId(1L);
        model.setMaintenanceId("MNT001");
        model.setMaintenaceDescription("Engine overhaul");
        model.setMaintenanceEntryDate(entryDate);
        model.setMaintenanceEndDate(endDate);
        model.setMaintenanceReleaseDate(releaseDate);
        model.setMaintenanceCrewGroup("A");
        model.setMaintenancePrice(5000);
        model.setEngineCode("ENG001");
        model.setTrainId(100L);

        responseDTO = new MaintenanceResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setMaintenanceId("MNT001");
        responseDTO.setMaintenanceDescription("Engine overhaul");
        responseDTO.setMaintenanceEntryDate(entryDate);
        responseDTO.setMaintenanceEndDate(endDate);
        responseDTO.setMaintenanceReleaseDate(releaseDate);
        responseDTO.setMaintenanceCrewGroup("A");
        responseDTO.setMaintenancePrice(5000);
        responseDTO.setEngineCode("ENG001");
        responseDTO.setTrainId(100L);

        requestDTO = new MaintenanceRequestDTO();
        requestDTO.setMaintenanceId("MNT001");
        requestDTO.setMaintenanceDescription("Engine overhaul");
        requestDTO.setMaintenanceEntryDate(entryDate);
        requestDTO.setMaintenanceEndDate(endDate);
        requestDTO.setMaintenanceReleaseDate(releaseDate);
        requestDTO.setMaintenanceCrewGroup("A");
        requestDTO.setMaintenancePrice(5000);
        requestDTO.setEngineCode("ENG001");
        requestDTO.setTrainId(100L);
    }

    // ---- getMaintenanceById ----

    @Test
    void getMaintenanceById_shouldReturnMaintenance_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        MaintenanceResponseDTO result = maintenanceService.getMaintenanceById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getMaintenanceById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> maintenanceService.getMaintenanceById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Maintenance not found with id: 99");
    }

    // ---- getMaintenanceByCode ----

    @Test
    void getMaintenanceByCode_shouldReturnMaintenance_whenExists() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        MaintenanceResponseDTO result = maintenanceService.getMaintenanceByCode("MNT001");

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    @Test
    void getMaintenanceByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findAll()).thenReturn(List.of(model)); // no matching code

        assertThatThrownBy(() -> maintenanceService.getMaintenanceByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Maintenance not found with code: UNKNOWN");
    }

    // ---- createMaintenance ----

    @Test
    void createMaintenance_shouldSaveAndReturn() {
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        MaintenanceResponseDTO result = maintenanceService.createMaintenance(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(model);
        verify(mapper).toResponse(model);
    }

    // ---- updateMaintenance ----

    @Test
    void updateMaintenance_shouldUpdateAndReturn_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(any(MaintenanceModel.class))).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        MaintenanceResponseDTO result = maintenanceService.updateMaintenance(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(any(MaintenanceModel.class));
    }

    @Test
    void updateMaintenance_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> maintenanceService.updateMaintenance(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Maintenance not found with id: 99");
    }

    // ---- deleteMaintenance ----

    @Test
    void deleteMaintenance_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        maintenanceService.deleteMaintenance(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteMaintenance_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> maintenanceService.deleteMaintenance(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Maintenance not found with id: 99");
    }

    // ---- getAllMaintenances ----

    @Test
    void getAllMaintenances_shouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<MaintenanceResponseDTO> result = maintenanceService.getAllMaintenances();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    // ---- getMaintenancesByCrewId ----

    @Test
    void getMaintenancesByCrewId_shouldReturnList() {
        when(repository.findByMaintenanceCrewGroup("A")).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<MaintenanceResponseDTO> result = maintenanceService.getMaintenancesByCrewId("A");

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByMaintenanceCrewGroup("A");
        verify(mapper).toResponse(model);
    }

    // ========== FALLBACK TESTS ==========
    // Fallback methods are public, so we can call them directly.

    @Test
    void handleGetMaintenanceFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getMaintenanceFallback(1L, t)).thenReturn(responseDTO);

        MaintenanceResponseDTO result = maintenanceService.handleGetMaintenanceFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getMaintenanceFallback(1L, t);
    }

    @Test
    void handleGetMaintenanceFallback_withRequest_shouldDelegateToFallbackWithMinusOne() {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getMaintenanceFallback(-1L, t)).thenReturn(responseDTO);

        MaintenanceResponseDTO result = maintenanceService.handleGetMaintenanceFallback(requestDTO, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getMaintenanceFallback(-1L, t);
    }

    @Test
    void handleGetMaintenanceFallback_withIdAndRequest_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getMaintenanceFallback(1L, t)).thenReturn(responseDTO);

        MaintenanceResponseDTO result = maintenanceService.handleGetMaintenanceFallback(1L, requestDTO, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getMaintenanceFallback(1L, t);
    }

    @Test
    void handleGetMaintenanceByCodeFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(serviceFallback.getMaintenanceFallback("MNT001", t)).thenReturn(responseDTO);

        MaintenanceResponseDTO result = maintenanceService.handleGetMaintenanceByCodeFallback("MNT001", t);

        assertThat(result).isEqualTo(responseDTO);
        verify(serviceFallback).getMaintenanceFallback("MNT001", t);
    }

    @Test
    void handleGetMaintenancesFallbackList_withThrowable_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<MaintenanceResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getMaintenancesFallbackList(t)).thenReturn(fallbackList);

        List<MaintenanceResponseDTO> result = maintenanceService.handleGetMaintenancesFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getMaintenancesFallbackList(t);
    }

    @Test
    void handleGetMaintenancesFallbackList_withStringParam_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<MaintenanceResponseDTO> fallbackList = List.of(responseDTO);
        when(serviceFallback.getMaintenancesFallbackList(t)).thenReturn(fallbackList);

        List<MaintenanceResponseDTO> result = maintenanceService.handleGetMaintenancesFallbackList("A", t);

        assertThat(result).isEqualTo(fallbackList);
        verify(serviceFallback).getMaintenancesFallbackList(t);
    }
}