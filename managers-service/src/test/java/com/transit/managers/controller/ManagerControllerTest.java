package com.transit.managers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.managers.dto.request.ManagerRequestDTO;
import com.transit.managers.dto.response.ManagerResponseDTO;
import com.transit.managers.service.ManagerService;
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

@WebMvcTest(ManagerController.class)
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ManagerService managerService;

    private ManagerResponseDTO responseDTO;
    private ManagerRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date contractDate = Date.valueOf("2024-01-01");
        // Updated code to satisfy @Size(min=7, max=13)
        String validCode = "MGR0011"; // 7 characters

        responseDTO = new ManagerResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCode(validCode);
        responseDTO.setSalary(75000L);
        responseDTO.setContractDate(contractDate);
        responseDTO.setFirstName("John");
        responseDTO.setSecondName("Doe");
        responseDTO.setManagerGroup("A");

        requestDTO = new ManagerRequestDTO();
        requestDTO.setCode(validCode);
        requestDTO.setSalary(75000L);
        requestDTO.setContractDate(contractDate);
        requestDTO.setFirstName("John");
        requestDTO.setSecondName("Doe");
        requestDTO.setManagerGroup("A");
    }

    // ---- GET /api/managers ----

    @Test
    void getAllManagers_shouldReturnList() throws Exception {
        when(managerService.getAllManagers()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/managers")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.managerResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.managerResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.managerResponseDTOList[0].code", is("MGR0011")))
                .andExpect(jsonPath("$._embedded.managerResponseDTOList[0].salary", is(75000)))
                .andExpect(jsonPath("$._embedded.managerResponseDTOList[0].firstName", is("John")))
                .andExpect(jsonPath("$._embedded.managerResponseDTOList[0]._links.self.href", containsString("/api/managers/1")))
                .andExpect(jsonPath("$._embedded.managerResponseDTOList[0]._links.all-managers.href", containsString("/api/managers")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/managers")));

        verify(managerService).getAllManagers();
    }

    // ---- GET /api/managers/{id} ----

    @Test
    void getManagerById_shouldReturnManager() throws Exception {
        when(managerService.getManagerById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/managers/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("MGR0011")))
                .andExpect(jsonPath("$.salary", is(75000)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/managers/1")))
                .andExpect(jsonPath("$._links.all-managers.href", containsString("/api/managers")));

        verify(managerService).getManagerById(1L);
    }

    // ---- GET /api/managers/code/{code} ----

    @Test
    void getManagerByCode_shouldReturnManager() throws Exception {
        when(managerService.getManagerByCode("MGR0011")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/managers/code/MGR0011")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("MGR0011")))
                .andExpect(jsonPath("$.salary", is(75000)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/managers/1")))
                .andExpect(jsonPath("$._links.all-managers.href", containsString("/api/managers")));

        verify(managerService).getManagerByCode("MGR0011");
    }

    // ---- POST /api/managers ----

    @Test
    void createManager_shouldReturnCreated() throws Exception {
        when(managerService.createManager(any(ManagerRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("MGR0011")))
                .andExpect(jsonPath("$.salary", is(75000)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/managers/1")))
                .andExpect(jsonPath("$._links.all-managers.href", containsString("/api/managers")));

        verify(managerService).createManager(any(ManagerRequestDTO.class));
    }

    // ---- PUT /api/managers/{id} ----

    @Test
    void updateManager_shouldReturnUpdated() throws Exception {
        when(managerService.updateManager(eq(1L), any(ManagerRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/managers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("MGR0011")))
                .andExpect(jsonPath("$.salary", is(75000)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/managers/1")))
                .andExpect(jsonPath("$._links.all-managers.href", containsString("/api/managers")));

        verify(managerService).updateManager(eq(1L), any(ManagerRequestDTO.class));
    }

    // ---- DELETE /api/managers/{id} ----

    @Test
    void deleteManager_shouldReturnNoContent() throws Exception {
        doNothing().when(managerService).deleteManager(1L);

        mockMvc.perform(delete("/api/managers/1"))
                .andExpect(status().isNoContent());

        verify(managerService).deleteManager(1L);
    }
}