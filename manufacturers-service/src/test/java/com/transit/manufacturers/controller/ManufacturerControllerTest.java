package com.transit.manufacturers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.manufacturers.dto.request.ManufacturerRequestDTO;
import com.transit.manufacturers.dto.response.ManufacturerResponseDTO;
import com.transit.manufacturers.service.ManufacturerService;
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

@WebMvcTest(ManufacturerController.class)
class ManufacturerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ManufacturerService manufacturerService;

    private ManufacturerResponseDTO responseDTO;
    private ManufacturerRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date foundingDate = Date.valueOf("2020-01-01");

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

    // ---- GET /api/manufacturers ----

    @Test
    void getAllManufacturers_shouldReturnList() throws Exception {
        when(manufacturerService.getAllManufacturers()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/manufacturers")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.manufacturerResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.manufacturerResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.manufacturerResponseDTOList[0].manufacturerId", is("SIEMENS")))
                .andExpect(jsonPath("$._embedded.manufacturerResponseDTOList[0].countryOfOrigin", is("Germany")))
                .andExpect(jsonPath("$._embedded.manufacturerResponseDTOList[0]._links.self.href", containsString("/api/manufacturers/1")))
                .andExpect(jsonPath("$._embedded.manufacturerResponseDTOList[0]._links.all-manufacturers.href", containsString("/api/manufacturers")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/manufacturers")));

        verify(manufacturerService).getAllManufacturers();
    }

    // ---- GET /api/manufacturers/{id} ----

    @Test
    void getManufacturerById_shouldReturnManufacturer() throws Exception {
        when(manufacturerService.getManufacturerById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/manufacturers/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.manufacturerId", is("SIEMENS")))
                .andExpect(jsonPath("$.countryOfOrigin", is("Germany")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/manufacturers/1")))
                .andExpect(jsonPath("$._links.all-manufacturers.href", containsString("/api/manufacturers")));

        verify(manufacturerService).getManufacturerById(1L);
    }

    // ---- GET /api/manufacturers/code/{manufacturerId} ----

    @Test
    void getManufacturerByCode_shouldReturnManufacturer() throws Exception {
        when(manufacturerService.getManufacturerByCode("SIEMENS")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/manufacturers/code/SIEMENS")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.manufacturerId", is("SIEMENS")))
                .andExpect(jsonPath("$.countryOfOrigin", is("Germany")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/manufacturers/1")))
                .andExpect(jsonPath("$._links.all-manufacturers.href", containsString("/api/manufacturers")));

        verify(manufacturerService).getManufacturerByCode("SIEMENS");
    }

    // ---- POST /api/manufacturers ----

    @Test
    void createManufacturer_shouldReturnCreated() throws Exception {
        when(manufacturerService.createManufacturer(any(ManufacturerRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.manufacturerId", is("SIEMENS")))
                .andExpect(jsonPath("$.countryOfOrigin", is("Germany")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/manufacturers/1")))
                .andExpect(jsonPath("$._links.all-manufacturers.href", containsString("/api/manufacturers")));

        verify(manufacturerService).createManufacturer(any(ManufacturerRequestDTO.class));
    }

    // ---- PUT /api/manufacturers/{id} ----

    @Test
    void updateManufacturer_shouldReturnUpdated() throws Exception {
        when(manufacturerService.updateManufacturer(eq(1L), any(ManufacturerRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/manufacturers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.manufacturerId", is("SIEMENS")))
                .andExpect(jsonPath("$.countryOfOrigin", is("Germany")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/manufacturers/1")))
                .andExpect(jsonPath("$._links.all-manufacturers.href", containsString("/api/manufacturers")));

        verify(manufacturerService).updateManufacturer(eq(1L), any(ManufacturerRequestDTO.class));
    }

    // ---- DELETE /api/manufacturers/{id} ----

    @Test
    void deleteManufacturer_shouldReturnNoContent() throws Exception {
        doNothing().when(manufacturerService).deleteManufacturer(1L);

        mockMvc.perform(delete("/api/manufacturers/1"))
                .andExpect(status().isNoContent());

        verify(manufacturerService).deleteManufacturer(1L);
    }
}