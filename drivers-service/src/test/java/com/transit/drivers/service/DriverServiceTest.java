package com.transit.drivers.service;

import com.transit.drivers.dto.mapper.DriverMapper;
import com.transit.drivers.dto.request.DriverRequestDTO;
import com.transit.drivers.dto.response.DriverResponseDTO;
import com.transit.drivers.fallback.DriverServiceFallback;
import com.transit.drivers.model.DriverModel;
import com.transit.drivers.repository.DriverRepository;
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
class DriverServiceTest {

    @Mock
    private DriverRepository repository;

    @Mock
    private DriverMapper mapper;

    @Mock
    private DriverServiceFallback fallback;

    @InjectMocks
    private DriverService driverService;

    private DriverModel model;
    private DriverResponseDTO responseDTO;
    private DriverRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date contractDate = Date.valueOf("2024-01-01");
        Date dateOfBirth = Date.valueOf("1990-05-15");

        model = new DriverModel();
        model.setId(1L);
        model.setCode("DRV001");
        model.setSalary(75000L);
        model.setContractDate(contractDate);
        model.setDateOfBirth(dateOfBirth);
        model.setFirstName("John");
        model.setSecondName("Doe");
        model.setCapacitatedCode("A");

        responseDTO = new DriverResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCode("DRV001");
        responseDTO.setSalary(75000L);
        responseDTO.setContractDate(contractDate);
        responseDTO.setDateOfBirth(dateOfBirth);
        responseDTO.setFirstName("John");
        responseDTO.setSecondName("Doe");
        responseDTO.setCapacitatedCode("A");

        requestDTO = new DriverRequestDTO();
        requestDTO.setCode("DRV001");
        requestDTO.setSalary(75000L);
        requestDTO.setContractDate(contractDate);
        requestDTO.setDateOfBirth(dateOfBirth);
        requestDTO.setFirstName("John");
        requestDTO.setSecondName("Doe");
        requestDTO.setCapacitatedCode("A");
    }

    // ---- getDriverById ----

    @Test
    void getDriverById_shouldReturnDriver_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        DriverResponseDTO result = driverService.getDriverById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getDriverById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.getDriverById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Driver not found with id: 99");
    }

    // ---- getDriverByCode ----

    @Test
    void getDriverByCode_shouldReturnDriver_whenExists() {
        when(repository.findByCode("DRV001")).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        DriverResponseDTO result = driverService.getDriverByCode("DRV001");

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findByCode("DRV001");
        verify(mapper).toResponse(model);
    }

    @Test
    void getDriverByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.getDriverByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Driver not found with code: UNKNOWN");
    }

    // ---- createDriver ----

    @Test
    void createDriver_shouldSaveAndReturn() {
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        DriverResponseDTO result = driverService.createDriver(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(model);
        verify(mapper).toResponse(model);
    }

    // ---- updateDriver ----

    @Test
    void updateDriver_shouldUpdateAndReturn_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(any(DriverModel.class))).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        DriverResponseDTO result = driverService.updateDriver(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(any(DriverModel.class));
    }

    @Test
    void updateDriver_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.updateDriver(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Driver not found with id: 99");
    }

    // ---- deleteDriver ----

    @Test
    void deleteDriver_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        driverService.deleteDriver(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteDriver_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> driverService.deleteDriver(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Driver not found with id: 99");
    }

    // ---- getAllDrivers ----

    @Test
    void getAllDrivers_shouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<DriverResponseDTO> result = driverService.getAllDrivers();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    // ========== FALLBACK TESTS ==========
    // Fallback methods are public, so we can call them directly.

    @Test
    void handleGetDriverFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getDriverFallback(1L, t)).thenReturn(responseDTO);

        DriverResponseDTO result = driverService.handleGetDriverFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getDriverFallback(1L, t);
    }

    @Test
    void handleGetDriverByCodeFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getDriverFallback("DRV001", t)).thenReturn(responseDTO);

        DriverResponseDTO result = driverService.handleGetDriverByCodeFallback("DRV001", t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getDriverFallback("DRV001", t);
    }

    @Test
    void handleGetDriversFallbackList_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<DriverResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getDriversFallbackList(t)).thenReturn(fallbackList);

        List<DriverResponseDTO> result = driverService.handleGetDriversFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getDriversFallbackList(t);
    }
}