package com.transit.drivers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.drivers.dto.request.DriverRequestDTO;
import com.transit.drivers.dto.response.DriverResponseDTO;
import com.transit.drivers.service.DriverService;
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

@WebMvcTest(DriverController.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DriverService driverService;

    private DriverResponseDTO responseDTO;
    private DriverRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date contractDate = Date.valueOf("2024-01-01");
        Date dateOfBirth = Date.valueOf("1990-05-15");
        // Valid code: length 8 (min=8, max=10)
        String validCode = "DRV00101";

        responseDTO = new DriverResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCode(validCode);
        responseDTO.setSalary(75000L);
        responseDTO.setContractDate(contractDate);
        responseDTO.setDateOfBirth(dateOfBirth);
        responseDTO.setFirstName("John");
        responseDTO.setSecondName("Doe");
        responseDTO.setCapacitatedCode("A");

        requestDTO = new DriverRequestDTO();
        requestDTO.setCode(validCode);
        requestDTO.setSalary(75000L);
        requestDTO.setContractDate(contractDate);
        requestDTO.setDateOfBirth(dateOfBirth);
        requestDTO.setFirstName("John");
        requestDTO.setSecondName("Doe");
        requestDTO.setCapacitatedCode("A");
    }

    // ---- GET /api/drivers ----

    @Test
    void getAllDrivers_shouldReturnList() throws Exception {
        when(driverService.getAllDrivers()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/drivers")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.driverResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.driverResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.driverResponseDTOList[0].code", is("DRV00101")))
                .andExpect(jsonPath("$._embedded.driverResponseDTOList[0]._links.self.href", containsString("/api/drivers/1")))
                .andExpect(jsonPath("$._embedded.driverResponseDTOList[0]._links.all-drivers.href", containsString("/api/drivers")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/drivers")));

        verify(driverService).getAllDrivers();
    }

    // ---- GET /api/drivers/{id} ----

    @Test
    void getDriverById_shouldReturnDriver() throws Exception {
        when(driverService.getDriverById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/drivers/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("DRV00101")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/drivers/1")))
                .andExpect(jsonPath("$._links.all-drivers.href", containsString("/api/drivers")));

        verify(driverService).getDriverById(1L);
    }

    // ---- GET /api/drivers/code/{code} ----

    @Test
    void getDriverByCode_shouldReturnDriver() throws Exception {
        when(driverService.getDriverByCode("DRV00101")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/drivers/code/DRV00101")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("DRV00101")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/drivers/1")))
                .andExpect(jsonPath("$._links.all-drivers.href", containsString("/api/drivers")));

        verify(driverService).getDriverByCode("DRV00101");
    }

    // ---- POST /api/drivers ----

    @Test
    void createDriver_shouldReturnCreated() throws Exception {
        when(driverService.createDriver(any(DriverRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("DRV00101")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/drivers/1")))
                .andExpect(jsonPath("$._links.all-drivers.href", containsString("/api/drivers")));

        verify(driverService).createDriver(any(DriverRequestDTO.class));
    }

    // ---- PUT /api/drivers/{id} ----

    @Test
    void updateDriver_shouldReturnUpdated() throws Exception {
        when(driverService.updateDriver(eq(1L), any(DriverRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("DRV00101")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/drivers/1")))
                .andExpect(jsonPath("$._links.all-drivers.href", containsString("/api/drivers")));

        verify(driverService).updateDriver(eq(1L), any(DriverRequestDTO.class));
    }

    // ---- DELETE /api/drivers/{id} ----

    @Test
    void deleteDriver_shouldReturnNoContent() throws Exception {
        doNothing().when(driverService).deleteDriver(1L);

        mockMvc.perform(delete("/api/drivers/1"))
                .andExpect(status().isNoContent());

        verify(driverService).deleteDriver(1L);
    }
}