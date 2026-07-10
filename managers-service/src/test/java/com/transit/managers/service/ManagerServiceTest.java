package com.transit.managers.service;

import com.transit.managers.dto.mapper.ManagerMapper;
import com.transit.managers.dto.request.ManagerRequestDTO;
import com.transit.managers.dto.response.ManagerResponseDTO;
import com.transit.managers.fallback.ManagerServiceFallback;
import com.transit.managers.model.ManagerModel;
import com.transit.managers.repository.ManagerRepository;
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
class ManagerServiceTest {

    @Mock
    private ManagerRepository repository;

    @Mock
    private ManagerMapper mapper;

    @Mock
    private ManagerServiceFallback fallback;

    @InjectMocks
    private ManagerService managerService;

    private ManagerModel model;
    private ManagerResponseDTO responseDTO;
    private ManagerRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date contractDate = Date.valueOf("2024-01-01");

        model = new ManagerModel();
        model.setId(1L);
        model.setCode("MGR001");
        model.setSalary(75000L);
        model.setContractDate(contractDate);
        model.setFirstName("John");
        model.setSecondName("Doe");
        model.setManagerGroup("A");

        responseDTO = new ManagerResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCode("MGR001");
        responseDTO.setSalary(75000L);
        responseDTO.setContractDate(contractDate);
        responseDTO.setFirstName("John");
        responseDTO.setSecondName("Doe");
        responseDTO.setManagerGroup("A");

        requestDTO = new ManagerRequestDTO();
        requestDTO.setCode("MGR001");
        requestDTO.setSalary(75000L);
        requestDTO.setContractDate(contractDate);
        requestDTO.setFirstName("John");
        requestDTO.setSecondName("Doe");
        requestDTO.setManagerGroup("A");
    }

    // ---- getManagerById ----

    @Test
    void getManagerById_shouldReturnManager_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ManagerResponseDTO result = managerService.getManagerById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getManagerById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> managerService.getManagerById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Manager not found with id: 99");
    }

    // ---- getManagerByCode ----

    @Test
    void getManagerByCode_shouldReturnManager_whenExists() {
        when(repository.findByCode("MGR001")).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ManagerResponseDTO result = managerService.getManagerByCode("MGR001");

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findByCode("MGR001");
        verify(mapper).toResponse(model);
    }

    @Test
    void getManagerByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> managerService.getManagerByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Manager not found with code: UNKNOWN");
    }

    // ---- createManager ----

    @Test
    void createManager_shouldSaveAndReturn() {
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ManagerResponseDTO result = managerService.createManager(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(model);
        verify(mapper).toResponse(model);
    }

    // ---- updateManager ----

    @Test
    void updateManager_shouldUpdateAndReturn_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(any(ManagerModel.class))).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ManagerResponseDTO result = managerService.updateManager(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(any(ManagerModel.class));
    }

    @Test
    void updateManager_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> managerService.updateManager(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Manager not found with id: 99");
    }

    // ---- deleteManager ----

    @Test
    void deleteManager_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        managerService.deleteManager(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteManager_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> managerService.deleteManager(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Manager not found with id: 99");
    }

    // ---- getAllManagers ----

    @Test
    void getAllManagers_shouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<ManagerResponseDTO> result = managerService.getAllManagers();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    // ========== FALLBACK TESTS ==========

    @Test
    void handleGetManagerFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getManagerFallback(1L, t)).thenReturn(responseDTO);

        ManagerResponseDTO result = managerService.handleGetManagerFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getManagerFallback(1L, t);
    }

    @Test
    void handleGetManagerByCodeFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getManagerFallback("CODE", t)).thenReturn(responseDTO);

        ManagerResponseDTO result = managerService.handleGetManagerByCodeFallback("CODE", t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getManagerFallback("CODE", t);
    }

    @Test
    void handleGetManagersFallbackList_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<ManagerResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getManagersFallbackList(t)).thenReturn(fallbackList);

        List<ManagerResponseDTO> result = managerService.handleGetManagersFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getManagersFallbackList(t);
    }
}