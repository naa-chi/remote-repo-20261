package com.transit.stations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.stations.dto.request.StationRequestDTO;
import com.transit.stations.dto.response.StationResponseDTO;
import com.transit.stations.service.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StationController.class)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StationService stationService;

    private StationResponseDTO responseDTO;
    private StationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new StationResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStationCode("STN001");
        responseDTO.setCityCode("LON");
        responseDTO.setCityName("London");
        responseDTO.setLineCode1(10);
        responseDTO.setLineCode2(20);
        responseDTO.setLine1Length(42L);
        responseDTO.setLine1PeopleServed(500000L);
        responseDTO.setLine2Length(42L);
        responseDTO.setLine2PeopleServed(500000L);

        requestDTO = new StationRequestDTO();
        requestDTO.setStationCode("STN001");
        requestDTO.setCityCode("LON");
        requestDTO.setLineCode1(10);
        requestDTO.setLineCode2(20);
    }

    // ---- GET /api/stations ----

    @Test
    void getAllStations_shouldReturnList() throws Exception {
        when(stationService.getAllStations()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/stations")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stationResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0].stationCode", is("STN001")))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0]._links.self.href", containsString("/api/stations/1")))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0]._links.all-stations.href", containsString("/api/stations")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/stations")));

        verify(stationService).getAllStations();
    }

    // ---- GET /api/stations/{id} ----

    @Test
    void getStationById_shouldReturnStation() throws Exception {
        when(stationService.getStationById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/stations/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.stationCode", is("STN001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/stations/1")))
                .andExpect(jsonPath("$._links.all-stations.href", containsString("/api/stations")));

        verify(stationService).getStationById(1L);
    }

    // ---- GET /api/stations/code/{stationCode} ----

    @Test
    void getStationByCode_shouldReturnStation() throws Exception {
        when(stationService.getStationByCode("STN001")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/stations/code/STN001")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.stationCode", is("STN001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/stations/1")))
                .andExpect(jsonPath("$._links.all-stations.href", containsString("/api/stations")));

        verify(stationService).getStationByCode("STN001");
    }

    // ---- GET /api/stations/city/{cityCode} ----

    @Test
    void getStationsByCity_shouldReturnList() throws Exception {
        when(stationService.getStationsByCity("LON")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/stations/city/LON")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stationResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0]._links.self.href", containsString("/api/stations/1")))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0]._links.all-stations.href", containsString("/api/stations")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/stations/city/LON")));

        verify(stationService).getStationsByCity("LON");
    }

    // ---- GET /api/stations/line/{lineCode} ----

    @Test
    void getStationsByLine_shouldReturnList() throws Exception {
        when(stationService.getStationsByLine(10)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/stations/line/10")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stationResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0]._links.self.href", containsString("/api/stations/1")))
                .andExpect(jsonPath("$._embedded.stationResponseDTOList[0]._links.all-stations.href", containsString("/api/stations")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/stations/line/10")));

        verify(stationService).getStationsByLine(10);
    }

    // ---- POST /api/stations ----

    @Test
    void createStation_shouldReturnCreatedStation() throws Exception {
        when(stationService.createStation(any(StationRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.stationCode", is("STN001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/stations/1")))
                .andExpect(jsonPath("$._links.all-stations.href", containsString("/api/stations")));

        verify(stationService).createStation(any(StationRequestDTO.class));
    }

    // ---- PUT /api/stations/{id} ----

    @Test
    void updateStation_shouldReturnUpdatedStation() throws Exception {
        when(stationService.updateStation(eq(1L), any(StationRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/stations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.stationCode", is("STN001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/stations/1")))
                .andExpect(jsonPath("$._links.all-stations.href", containsString("/api/stations")));

        verify(stationService).updateStation(eq(1L), any(StationRequestDTO.class));
    }

    // ---- DELETE /api/stations/{id} ----

    @Test
    void deleteStation_shouldReturnNoContent() throws Exception {
        doNothing().when(stationService).deleteStation(1L);

        mockMvc.perform(delete("/api/stations/1"))
                .andExpect(status().isNoContent());

        verify(stationService).deleteStation(1L);
    }
}