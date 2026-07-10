package com.transit.trains.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.trains.dto.request.TrainRequestDTO;
import com.transit.trains.dto.response.TrainResponseDTO;
import com.transit.trains.service.TrainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainController.class)
class TrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TrainService trainService;

    private TrainResponseDTO responseDTO;
    private TrainRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new TrainResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCode("T001");
        // populate other fields if needed for tests

        requestDTO = new TrainRequestDTO();
        requestDTO.setCode("T001");
        requestDTO.setManufacturerId("Siemens");
        requestDTO.setEngineId(100L);
        requestDTO.setCarAmount(10);
        requestDTO.setCostPerCar(5000);
        requestDTO.setProductionDate(Date.valueOf("2023-01-01"));
    }

    @Test
    void getTrain_shouldReturnTrain_whenExists() throws Exception {
        when(trainService.getTrainById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/trains/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("T001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/trains/1")))
                .andExpect(jsonPath("$._links.all-trains.href", containsString("/api/trains/all")));

        verify(trainService).getTrainById(1L);
    }

    @Test
    void getTrainByCode_shouldReturnTrain_whenExists() throws Exception {
        when(trainService.getTrainByCode("T001")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/trains/code/T001")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("T001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/trains/code/T001")))
                .andExpect(jsonPath("$._links.train-details.href", containsString("/api/trains/1")))
                .andExpect(jsonPath("$._links.all-trains.href", containsString("/api/trains/all")));

        verify(trainService).getTrainByCode("T001");
    }

    @Test
    void getTrainsByManufacturerId_shouldReturnList() throws Exception {
        when(trainService.getTrainsByManufacturerId("Siemens")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/trains/manufacturer/Siemens")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.trainResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.trainResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.trainResponseDTOList[0]._links.train-details.href", containsString("/api/trains/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/trains/manufacturer/Siemens")))
                .andExpect(jsonPath("$._links.all-trains.href", containsString("/api/trains/all")));

        verify(trainService).getTrainsByManufacturerId("Siemens");
    }

    @Test
    void getAllTrains_shouldReturnAll() throws Exception {
        when(trainService.getAllTrains()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/trains/all")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.trainResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.trainResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.trainResponseDTOList[0]._links.self.href", containsString("/api/trains/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/trains/all")));

        verify(trainService).getAllTrains();
    }

    @Test
    void getTrainsByEngineId_shouldReturnList() throws Exception {
        when(trainService.getTrainsByEngineId(100L)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/trains/engine/100")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.trainResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.trainResponseDTOList[0].id", is(1)))
                // Controller adds "self" link, not "train-details"
                .andExpect(jsonPath("$._embedded.trainResponseDTOList[0]._links.self.href", containsString("/api/trains/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/trains/engine/100")))
                .andExpect(jsonPath("$._links.all-trains.href", containsString("/api/trains/all")));

        verify(trainService).getTrainsByEngineId(100L);
    }

    @Test
    void createTrain_shouldReturnCreatedTrain() throws Exception {
        when(trainService.createTrain(any(TrainRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/trains/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("T001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/trains/1")))
                .andExpect(jsonPath("$._links.all-trains.href", containsString("/api/trains/all")));

        verify(trainService).createTrain(any(TrainRequestDTO.class));
    }

    @Test
    void deleteTrain_shouldReturnNoContent() throws Exception {
        doNothing().when(trainService).deleteTrain(1L);

        mockMvc.perform(delete("/api/trains/1"))
                .andExpect(status().isNoContent());

        verify(trainService).deleteTrain(1L);
    }
}