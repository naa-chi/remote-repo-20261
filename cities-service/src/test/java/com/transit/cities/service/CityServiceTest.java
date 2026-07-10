package com.transit.cities.service;

import com.transit.cities.dto.mapper.CityMapper;
import com.transit.cities.dto.request.CityRequestDTO;
import com.transit.cities.dto.response.CityResponseDTO;
import com.transit.cities.fallback.CityServiceFallback;
import com.transit.cities.model.CityModel;
import com.transit.cities.repository.CityRepository;
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
class CityServiceTest {

    @Mock
    private CityRepository repository;

    @Mock
    private CityMapper mapper;

    @Mock
    private CityServiceFallback fallback;

    @InjectMocks
    private CityService cityService;

    private CityModel model;
    private CityResponseDTO responseDTO;
    private CityRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date foundingDate = Date.valueOf("2024-01-01");

        model = new CityModel();
        model.setId(1L);
        model.setThreeLetterCityCode("LON");
        model.setFullCityName("London");
        model.setFoundingCityDate(foundingDate);
        model.setCityPopulation(9000000L);
        model.setCountryCode("GB");

        responseDTO = new CityResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setThreeLetterCityCode("LON");
        responseDTO.setFullCityName("London");
        responseDTO.setFoundingCityDate(foundingDate);
        responseDTO.setCityPopulation(9000000L);
        responseDTO.setCountryCode("GB");

        requestDTO = new CityRequestDTO();
        requestDTO.setThreeLetterCityCode("LON");
        requestDTO.setFullCityName("London");
        requestDTO.setFoundingCityDate(foundingDate);
        requestDTO.setCityPopulation(9000000L);
        requestDTO.setCountryCode("GB");
    }

    // ---- getCityById ----

    @Test
    void getCityById_shouldReturnCity_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        CityResponseDTO result = cityService.getCityById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getCityById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.getCityById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("City not found with id: 99");
    }

    // ---- getCityByCode ----

    @Test
    void getCityByCode_shouldReturnCity_whenExists() {
        when(repository.findByThreeLetterCityCode("LON")).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        CityResponseDTO result = cityService.getCityByCode("LON");

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findByThreeLetterCityCode("LON");
        verify(mapper).toResponse(model);
    }

    @Test
    void getCityByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findByThreeLetterCityCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.getCityByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("City not found with code: UNKNOWN");
    }

    // ---- createCity ----

    @Test
    void createCity_shouldSaveAndReturn() {
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        CityResponseDTO result = cityService.createCity(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(model);
        verify(mapper).toResponse(model);
    }

    // ---- updateCity ----

    @Test
    void updateCity_shouldUpdateAndReturn_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(any(CityModel.class))).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        CityResponseDTO result = cityService.updateCity(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(any(CityModel.class));
    }

    @Test
    void updateCity_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.updateCity(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("City not found with id: 99");
    }

    // ---- deleteCity ----

    @Test
    void deleteCity_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        cityService.deleteCity(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteCity_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> cityService.deleteCity(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("City not found with id: 99");
    }

    // ---- getAllCities ----

    @Test
    void getAllCities_shouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<CityResponseDTO> result = cityService.getAllCities();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    // ========== FALLBACK TESTS ==========
    // Fallback methods are public, so we can call them directly.

    @Test
    void handleGetCityFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getCityFallback(1L, t)).thenReturn(responseDTO);

        CityResponseDTO result = cityService.handleGetCityFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getCityFallback(1L, t);
    }

    @Test
    void handleGetCityByCodeFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getCityFallback("LON", t)).thenReturn(responseDTO);

        CityResponseDTO result = cityService.handleGetCityByCodeFallback("LON", t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getCityFallback("LON", t);
    }

    @Test
    void handleGetCitiesFallbackList_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<CityResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getCitiesFallbackList(t)).thenReturn(fallbackList);

        List<CityResponseDTO> result = cityService.handleGetCitiesFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getCitiesFallbackList(t);
    }
}