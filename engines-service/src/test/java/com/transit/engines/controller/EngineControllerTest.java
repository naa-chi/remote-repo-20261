package com.transit.engines.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.engines.dto.request.EngineRequestDTO;
import com.transit.engines.dto.response.EngineResponseDTO;
import com.transit.engines.service.EngineService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EngineController.class)
class EngineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EngineService engineService;

    private EngineResponseDTO responseDTO;
    private EngineRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date productionDate = Date.valueOf("2024-01-01");

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

    // ---- GET /api/engines/{id} ----

    @Test
    void getEngineById_shouldReturnEngine() throws Exception {
        when(engineService.getEngineById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/engines/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.engineId", is(100)))
                .andExpect(jsonPath("$.manufacturerId", is("SIEMENS")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/engines/1")))
                .andExpect(jsonPath("$._links.all-engines.href", containsString("/api/engines/all")));

        verify(engineService).getEngineById(1L);
    }

    // ---- GET /api/engines/manufacturer/{code} ----

    @Test
    void getEnginesByManufacturerCode_shouldReturnList() throws Exception {
        when(engineService.getEnginesByManufacturerId("SIEMENS")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/engines/manufacturer/SIEMENS")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.engineResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0].manufacturerId", is("SIEMENS")))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0]._links.self.href", containsString("/api/engines/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/engines/manufacturer/SIEMENS")))
                .andExpect(jsonPath("$._links.all-engines.href", containsString("/api/engines/all")));

        verify(engineService).getEnginesByManufacturerId("SIEMENS");
    }

    // ---- GET /api/engines/engineCode/{engineCode} ----

    @Test
    void getEnginesByEngineCode_shouldReturnList() throws Exception {
        when(engineService.getEnginesByEngineCode("E001")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/engines/engineCode/E001")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.engineResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0].engineCode", is("E001")))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0]._links.self.href", containsString("/api/engines/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/engines/engineCode/E001")))
                .andExpect(jsonPath("$._links.all-engines.href", containsString("/api/engines/all")));

        verify(engineService).getEnginesByEngineCode("E001");
    }

    // ---- GET /api/engines/horsepower/{horsepower} ----

    @Test
    void getEnginesByHorsepower_shouldReturnList() throws Exception {
        when(engineService.getEngineHorsepower(4400.0f)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/engines/horsepower/4400.0")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.engineResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0].engineHorsepower", is(4400.0)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0]._links.self.href", containsString("/api/engines/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/engines/horsepower/4400.0")))
                .andExpect(jsonPath("$._links.all-engines.href", containsString("/api/engines/all")));

        verify(engineService).getEngineHorsepower(4400.0f);
    }

    // ---- GET /api/engines/price/{price} ----

    @Test
    void getEnginesByPrice_shouldReturnList() throws Exception {
        when(engineService.getEnginePrice(150000.0f)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/engines/price/150000.0")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.engineResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0].enginePrice", is(150000.0)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0]._links.self.href", containsString("/api/engines/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/engines/price/150000.0")))
                .andExpect(jsonPath("$._links.all-engines.href", containsString("/api/engines/all")));

        verify(engineService).getEnginePrice(150000.0f);
    }

    // ---- GET /api/engines/weight/{weight} ----

    @Test
    void getEnginesByWeight_shouldReturnList() throws Exception {
        when(engineService.getEngineWeight(120.5f)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/engines/weight/120.5")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.engineResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0].engineWeight", is(120.5)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0]._links.self.href", containsString("/api/engines/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/engines/weight/120.5")))
                .andExpect(jsonPath("$._links.all-engines.href", containsString("/api/engines/all")));

        verify(engineService).getEngineWeight(120.5f);
    }

    // ---- GET /api/engines/all ----

    @Test
    void getAllEngines_shouldReturnList() throws Exception {
        when(engineService.getAllEngines()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/engines/all")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.engineResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.engineResponseDTOList[0]._links.self.href", containsString("/api/engines/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/engines/all")));

        verify(engineService).getAllEngines();
    }

    // ---- POST /api/engines/create ----

    @Test
    void createEngine_shouldReturnCreated() throws Exception {
        when(engineService.createEngine(any(EngineRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/engines/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.engineId", is(100)))
                .andExpect(jsonPath("$.manufacturerId", is("SIEMENS")))
                // No links are added in create response
                .andExpect(jsonPath("$._links").doesNotExist());

        verify(engineService).createEngine(any(EngineRequestDTO.class));
    }

    // ---- PUT /api/engines/{id} ----

    @Test
    void updateEngine_shouldReturnUpdated() throws Exception {
        when(engineService.updateEngine(eq(1L), any(EngineRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/engines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.engineId", is(100)))
                .andExpect(jsonPath("$.manufacturerId", is("SIEMENS")))
                // No links are added in update response
                .andExpect(jsonPath("$._links").doesNotExist());

        verify(engineService).updateEngine(eq(1L), any(EngineRequestDTO.class));
    }

    // ---- DELETE /api/engines/{id} ----

    @Test
    void deleteEngine_shouldReturnNoContent() throws Exception {
        doNothing().when(engineService).deleteEngine(1L);

        mockMvc.perform(delete("/api/engines/1"))
                .andExpect(status().isNoContent());

        verify(engineService).deleteEngine(1L);
    }
}