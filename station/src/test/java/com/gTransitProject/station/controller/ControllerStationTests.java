package com.gTransitProject.station.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gTransitProject.station.client.CityClient;
import com.gTransitProject.station.client.LineClient;
import com.gTransitProject.station.dto.CityDTO;
import com.gTransitProject.station.dto.LineDTO;
import com.gTransitProject.station.model.station;
import com.gTransitProject.station.service.ServiceStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerStation.class)
class ControllerStationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceStation stationServ;

    @MockitoBean
    private LineClient lineClient;

    @MockitoBean
    private CityClient cityClient;

    private station sampleStation;
    private LineDTO sampleLineDTO;
    private CityDTO sampleCityDTO;

    @BeforeEach
    void setUp() {
        sampleStation = new station();
        sampleStation.setStationId(1);
        sampleStation.setUniqueStationCode("STN001");
        sampleStation.setStationName("Grand Central Terminal");
        sampleStation.setCityCode("NYC");
        sampleStation.setLineNumber(4);

        sampleLineDTO = new LineDTO();
        // Set fields if needed
        sampleCityDTO = new CityDTO();
        // Set fields if needed
    }

    @Test
    void getStations_ShouldReturnCollectionWithEmbeddedStations() throws Exception {
        List<station> allStations = Arrays.asList(sampleStation);
        when(stationServ.getStations()).thenReturn(allStations);

        mockMvc.perform(get("/api/stations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.station[0].stationId").value(1))
                .andExpect(jsonPath("$._embedded.station[0].uniqueStationCode").value("STN001"))
                .andExpect(jsonPath("$._embedded.station[0].stationName").value("Grand Central Terminal"))
                .andExpect(jsonPath("$._embedded.station[0].cityCode").value("NYC"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getStationById_ShouldReturnSingleStationWithSelfLink() throws Exception {
        when(stationServ.getStationById(1)).thenReturn(sampleStation);

        mockMvc.perform(get("/api/stations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationId").value(1))
                .andExpect(jsonPath("$.stationName").value("Grand Central Terminal"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.allStations.href").exists());
    }

    @Test
    void getStationById_NotFound_ShouldReturn404() throws Exception {
        when(stationServ.getStationById(999)).thenReturn(null);

        mockMvc.perform(get("/api/stations/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveStation_ShouldCreateAndReturnStationWithLinks() throws Exception {
        when(stationServ.saveStation(any(station.class))).thenReturn(sampleStation);

        mockMvc.perform(post("/api/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleStation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stationId").value(1))
                .andExpect(jsonPath("$.uniqueStationCode").value("STN001"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void updateStation_ShouldModifyAndReturnStationWithLinks() throws Exception {
        when(stationServ.updateStation(eq(1), any(station.class))).thenReturn(sampleStation);

        mockMvc.perform(put("/api/stations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationId").value(1))
                .andExpect(jsonPath("$.lineNumber").value(4))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void updateStation_NotFound_ShouldReturn404() throws Exception {
        when(stationServ.updateStation(eq(999), any(station.class))).thenReturn(null);

        mockMvc.perform(put("/api/stations/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleStation)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStation_ShouldReturnNoContent() throws Exception {
        doNothing().when(stationServ).deleteStation(1);

        mockMvc.perform(delete("/api/stations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testLine_ShouldReturnLineFromClient() throws Exception {
        when(lineClient.getLineByNumber(4)).thenReturn(sampleLineDTO);

        mockMvc.perform(get("/api/stations/test-line/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // No HATEOAS for test endpoints
    }

    @Test
    void testCity_ShouldReturnCityFromClient() throws Exception {
        when(cityClient.getCityByCode("NYC")).thenReturn(sampleCityDTO);

        mockMvc.perform(get("/api/stations/test-city/NYC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // No HATEOAS for test endpoints
    }
}