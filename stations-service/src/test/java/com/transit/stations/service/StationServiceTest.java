package com.transit.stations.service;

import com.transit.stations.client.CityFeignClient;
import com.transit.stations.client.LineFeignClient;
import com.transit.stations.dto.CityDTO;
import com.transit.stations.dto.LineDTO;
import com.transit.stations.dto.mapper.StationMapper;
import com.transit.stations.dto.request.StationRequestDTO;
import com.transit.stations.dto.response.StationResponseDTO;
import com.transit.stations.fallback.StationServiceFallback;
import com.transit.stations.model.StationModel;
import com.transit.stations.repository.StationRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private StationRepository repository;

    @Mock
    private StationMapper mapper;

    @Mock
    private StationServiceFallback fallback;

    @Mock
    private CityFeignClient cityFeign;

    @Mock
    private LineFeignClient lineFeign;

    @InjectMocks
    private StationService stationService;

    private StationModel stationModel;
    private StationResponseDTO responseDTO;
    private StationRequestDTO requestDTO;
    private CityDTO cityDTO;
    private LineDTO lineDTO;

    @BeforeEach
    void setUp() {
        stationModel = new StationModel();
        stationModel.setId(1L);
        stationModel.setStationCode("STN001");
        stationModel.setCityCode("LON");
        stationModel.setLineCode1(10);
        stationModel.setLineCode2(20);
        stationModel.setLineCode3(null);
        stationModel.setLineCode4(null);

        responseDTO = new StationResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStationCode("STN001");
        responseDTO.setCityCode("LON");
        responseDTO.setLineCode1(10);
        responseDTO.setLineCode2(20);
        responseDTO.setLineCode3(null);
        responseDTO.setLineCode4(null);

        requestDTO = new StationRequestDTO();
        requestDTO.setStationCode("STN001");
        requestDTO.setCityCode("LON");
        requestDTO.setLineCode1(10);
        requestDTO.setLineCode2(20);
        requestDTO.setLineCode3(null);
        requestDTO.setLineCode4(null);

        cityDTO = new CityDTO();
        // cityDTO.setCityCode("LON");  // <-- REMOVED: this setter doesn't exist
        cityDTO.setFullCityName("London");

        lineDTO = new LineDTO();
        lineDTO.setLineCode(10);
        lineDTO.setLineLengthKM(42L);
        lineDTO.setPeopleServedMonthlyEstimate(500000L);
    }

    // ---- getStationById ----

    @Test
    void getStationById_shouldReturnEnrichedStation_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(stationModel));
        when(mapper.toResponse(stationModel)).thenReturn(responseDTO);
        when(cityFeign.getCityByCode("LON")).thenReturn(cityDTO);
        when(lineFeign.getLineByCode(10)).thenReturn(lineDTO);
        when(lineFeign.getLineByCode(20)).thenReturn(lineDTO);

        StationResponseDTO result = stationService.getStationById(1L);

        assertThat(result).isEqualTo(responseDTO);
        assertThat(result.getCityName()).isEqualTo("London");
        assertThat(result.getLine1Length()).isEqualTo(42L);
        assertThat(result.getLine1PeopleServed()).isEqualTo(500000L);
        verify(repository).findById(1L);
        verify(mapper).toResponse(stationModel);
        verify(cityFeign).getCityByCode("LON");
        verify(lineFeign, times(2)).getLineByCode(anyInt());
    }

    @Test
    void getStationById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stationService.getStationById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Station not found with id: 99");
    }

    // ---- getStationByCode ----

    @Test
    void getStationByCode_shouldReturnEnrichedStation_whenExists() {
        when(repository.findByStationCode("STN001")).thenReturn(Optional.of(stationModel));
        when(mapper.toResponse(stationModel)).thenReturn(responseDTO);
        when(cityFeign.getCityByCode("LON")).thenReturn(cityDTO);
        when(lineFeign.getLineByCode(10)).thenReturn(lineDTO);
        when(lineFeign.getLineByCode(20)).thenReturn(lineDTO);

        StationResponseDTO result = stationService.getStationByCode("STN001");

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findByStationCode("STN001");
        verify(mapper).toResponse(stationModel);
        verify(cityFeign).getCityByCode("LON");
        verify(lineFeign, times(2)).getLineByCode(anyInt());
    }

    @Test
    void getStationByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findByStationCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stationService.getStationByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Station not found with code: UNKNOWN");
    }

    // ---- getStationsByCity ----

    @Test
    void getStationsByCity_shouldReturnList() {
        when(repository.findByCityCode("LON")).thenReturn(List.of(stationModel));
        when(mapper.toResponse(stationModel)).thenReturn(responseDTO);
        when(cityFeign.getCityByCode("LON")).thenReturn(cityDTO);
        when(lineFeign.getLineByCode(10)).thenReturn(lineDTO);
        when(lineFeign.getLineByCode(20)).thenReturn(lineDTO);

        List<StationResponseDTO> result = stationService.getStationsByCity("LON");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCityName()).isEqualTo("London");
        verify(repository).findByCityCode("LON");
        verify(mapper).toResponse(stationModel);
        verify(cityFeign).getCityByCode("LON");
    }

    // ---- getStationsByLine ----

    @Test
    void getStationsByLine_shouldReturnList() {
        when(repository.findByAnyLineCode(10)).thenReturn(List.of(stationModel));
        when(mapper.toResponse(stationModel)).thenReturn(responseDTO);
        when(cityFeign.getCityByCode("LON")).thenReturn(cityDTO);
        when(lineFeign.getLineByCode(10)).thenReturn(lineDTO);

        List<StationResponseDTO> result = stationService.getStationsByLine(10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLine1Length()).isEqualTo(42L);
        verify(repository).findByAnyLineCode(10);
        verify(mapper).toResponse(stationModel);
        verify(lineFeign).getLineByCode(10);
    }

    // ---- createStation ----

    @Test
    void createStation_shouldSaveAndReturnEnrichedStation() {
        when(mapper.toEntity(requestDTO)).thenReturn(stationModel);
        when(repository.save(stationModel)).thenReturn(stationModel);
        when(mapper.toResponse(stationModel)).thenReturn(responseDTO);
        when(cityFeign.getCityByCode("LON")).thenReturn(cityDTO);
        when(lineFeign.getLineByCode(10)).thenReturn(lineDTO);
        when(lineFeign.getLineByCode(20)).thenReturn(lineDTO);

        StationResponseDTO result = stationService.createStation(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        assertThat(result.getCityName()).isEqualTo("London");
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(stationModel);
        verify(mapper).toResponse(stationModel);
        verify(cityFeign).getCityByCode("LON");
        verify(lineFeign, times(2)).getLineByCode(anyInt());
    }

    // ---- updateStation ----

    @Test
    void updateStation_shouldUpdateAndReturn_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(stationModel));
        when(mapper.toEntity(requestDTO)).thenReturn(stationModel);
        when(repository.save(any(StationModel.class))).thenReturn(stationModel);
        when(mapper.toResponse(stationModel)).thenReturn(responseDTO);
        when(cityFeign.getCityByCode("LON")).thenReturn(cityDTO);
        when(lineFeign.getLineByCode(10)).thenReturn(lineDTO);
        when(lineFeign.getLineByCode(20)).thenReturn(lineDTO);

        StationResponseDTO result = stationService.updateStation(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(any(StationModel.class));
        verify(cityFeign).getCityByCode("LON");
    }

    @Test
    void updateStation_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stationService.updateStation(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Station not found with id: 99");
    }

    // ---- deleteStation ----

    @Test
    void deleteStation_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        stationService.deleteStation(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteStation_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> stationService.deleteStation(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Station not found with id: 99");
    }

    // ---- getAllStations ----

    @Test
    void getAllStations_shouldReturnAllEnrichedStations() {
        when(repository.findAll()).thenReturn(List.of(stationModel));
        when(mapper.toResponse(stationModel)).thenReturn(responseDTO);
        when(cityFeign.getCityByCode("LON")).thenReturn(cityDTO);
        when(lineFeign.getLineByCode(10)).thenReturn(lineDTO);
        when(lineFeign.getLineByCode(20)).thenReturn(lineDTO);

        List<StationResponseDTO> result = stationService.getAllStations();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCityName()).isEqualTo("London");
        verify(repository).findAll();
        verify(mapper).toResponse(stationModel);
        verify(cityFeign).getCityByCode("LON");
        verify(lineFeign, times(2)).getLineByCode(anyInt());
    }

    // ========== FALLBACK TESTS ==========

    @Test
    void handleGetStationFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getStationFallback(1L, t)).thenReturn(responseDTO);

        StationResponseDTO result = stationService.handleGetStationFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getStationFallback(1L, t);
    }

    @Test
    void handleGetStationByCodeFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getStationFallback("CODE", t)).thenReturn(responseDTO);

        StationResponseDTO result = stationService.handleGetStationByCodeFallback("CODE", t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getStationFallback("CODE", t);
    }

    @Test
    void handleGetStationsByCityFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<StationResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getStationsFallbackList(t)).thenReturn(fallbackList);

        List<StationResponseDTO> result = stationService.handleGetStationsByCityFallback("LON", t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getStationsFallbackList(t);
    }

    @Test
    void handleGetStationsByLineFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<StationResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getStationsFallbackList(t)).thenReturn(fallbackList);

        List<StationResponseDTO> result = stationService.handleGetStationsByLineFallback(10, t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getStationsFallbackList(t);
    }

    @Test
    void handleGetStationsFallbackList_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<StationResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getStationsFallbackList(t)).thenReturn(fallbackList);

        List<StationResponseDTO> result = stationService.handleGetStationsFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getStationsFallbackList(t);
    }
}