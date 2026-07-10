package com.transit.cities.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.cities.dto.request.CityRequestDTO;
import com.transit.cities.dto.response.CityResponseDTO;
import com.transit.cities.service.CityService;
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

@WebMvcTest(CityController.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CityService cityService;

    private CityResponseDTO responseDTO;
    private CityRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date foundingDate = Date.valueOf("2024-01-01");

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

    // ---- GET /api/cities ----

    @Test
    void getAllCities_shouldReturnList() throws Exception {
        when(cityService.getAllCities()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/cities")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cityResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.cityResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.cityResponseDTOList[0].threeLetterCityCode", is("LON")))
                .andExpect(jsonPath("$._embedded.cityResponseDTOList[0]._links.self.href", containsString("/api/cities/1")))
                .andExpect(jsonPath("$._embedded.cityResponseDTOList[0]._links.all-cities.href", containsString("/api/cities")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/cities")));

        verify(cityService).getAllCities();
    }

    // ---- GET /api/cities/{id} ----

    @Test
    void getCityById_shouldReturnCity() throws Exception {
        when(cityService.getCityById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/cities/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.threeLetterCityCode", is("LON")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/cities/1")))
                .andExpect(jsonPath("$._links.all-cities.href", containsString("/api/cities")));

        verify(cityService).getCityById(1L);
    }

    // ---- GET /api/cities/code/{cityCode} ----

    @Test
    void getCityByCode_shouldReturnCity() throws Exception {
        when(cityService.getCityByCode("LON")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/cities/code/LON")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.threeLetterCityCode", is("LON")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/cities/1")))
                .andExpect(jsonPath("$._links.all-cities.href", containsString("/api/cities")));

        verify(cityService).getCityByCode("LON");
    }

    // ---- POST /api/cities ----

    @Test
    void createCity_shouldReturnCreated() throws Exception {
        when(cityService.createCity(any(CityRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.threeLetterCityCode", is("LON")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/cities/1")))
                .andExpect(jsonPath("$._links.all-cities.href", containsString("/api/cities")));

        verify(cityService).createCity(any(CityRequestDTO.class));
    }

    // ---- PUT /api/cities/{id} ----

    @Test
    void updateCity_shouldReturnUpdated() throws Exception {
        when(cityService.updateCity(eq(1L), any(CityRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/cities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.threeLetterCityCode", is("LON")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/cities/1")))
                .andExpect(jsonPath("$._links.all-cities.href", containsString("/api/cities")));

        verify(cityService).updateCity(eq(1L), any(CityRequestDTO.class));
    }

    // ---- DELETE /api/cities/{id} ----

    @Test
    void deleteCity_shouldReturnNoContent() throws Exception {
        doNothing().when(cityService).deleteCity(1L);

        mockMvc.perform(delete("/api/cities/1"))
                .andExpect(status().isNoContent());

        verify(cityService).deleteCity(1L);
    }
}