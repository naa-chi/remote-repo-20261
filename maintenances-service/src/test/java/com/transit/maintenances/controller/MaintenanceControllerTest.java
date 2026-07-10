package com.transit.maintenances.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.maintenances.dto.request.MaintenanceRequestDTO;
import com.transit.maintenances.dto.response.MaintenanceResponseDTO;
import com.transit.maintenances.service.MaintenanceService;
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

@WebMvcTest(MaintenanceController.class)
class MaintenanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MaintenanceService maintenanceService;

    private MaintenanceResponseDTO responseDTO;
    private MaintenanceRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date entryDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-01-05");
        Date releaseDate = Date.valueOf("2024-01-10");

        responseDTO = new MaintenanceResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setMaintenanceId("MNT001");
        responseDTO.setMaintenanceDescription("Engine overhaul");
        responseDTO.setMaintenanceEntryDate(entryDate);
        responseDTO.setMaintenanceEndDate(endDate);
        responseDTO.setMaintenanceReleaseDate(releaseDate);
        responseDTO.setMaintenanceCrewGroup("A");
        responseDTO.setMaintenancePrice(5000);
        responseDTO.setEngineCode("ENG001");
        responseDTO.setTrainId(100L);

        requestDTO = new MaintenanceRequestDTO();
        requestDTO.setMaintenanceId("MNT001");
        requestDTO.setMaintenanceDescription("Engine overhaul");
        requestDTO.setMaintenanceEntryDate(entryDate);
        requestDTO.setMaintenanceEndDate(endDate);
        requestDTO.setMaintenanceReleaseDate(releaseDate);
        requestDTO.setMaintenanceCrewGroup("A");
        requestDTO.setMaintenancePrice(5000);
        requestDTO.setEngineCode("ENG001");
        requestDTO.setTrainId(100L);
    }

    // ---- GET /api/maintenances ----

    @Test
    void getAllMaintenances_shouldReturnList() throws Exception {
        when(maintenanceService.getAllMaintenances()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/maintenances")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList[0].maintenanceId", is("MNT001")))
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList[0]._links.self.href", containsString("/api/maintenances/1")))
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList[0]._links.all-maintenances.href", containsString("/api/maintenances")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/maintenances")));

        verify(maintenanceService).getAllMaintenances();
    }

    // ---- GET /api/maintenances/{id} ----

    @Test
    void getMaintenanceById_shouldReturnMaintenance() throws Exception {
        when(maintenanceService.getMaintenanceById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/maintenances/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.maintenanceId", is("MNT001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/maintenances/1")))
                .andExpect(jsonPath("$._links.all-maintenances.href", containsString("/api/maintenances")));

        verify(maintenanceService).getMaintenanceById(1L);
    }

    // ---- GET /api/maintenances/code/{maintenanceId} ----

    @Test
    void getMaintenanceByCode_shouldReturnMaintenance() throws Exception {
        when(maintenanceService.getMaintenanceByCode("MNT001")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/maintenances/code/MNT001")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.maintenanceId", is("MNT001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/maintenances/1")))
                .andExpect(jsonPath("$._links.all-maintenances.href", containsString("/api/maintenances")));

        verify(maintenanceService).getMaintenanceByCode("MNT001");
    }

    // ---- GET /api/maintenances/crew/{crewId} ----

    @Test
    void getMaintenancesByCrewId_shouldReturnList() throws Exception {
        when(maintenanceService.getMaintenancesByCrewId("A")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/maintenances/crew/A")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList[0].maintenanceCrewGroup", is("A")))
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList[0]._links.self.href", containsString("/api/maintenances/1")))
                .andExpect(jsonPath("$._embedded.maintenanceResponseDTOList[0]._links.all-maintenances.href", containsString("/api/maintenances")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/maintenances/crew/A")))
                .andExpect(jsonPath("$._links.all-maintenances.href", containsString("/api/maintenances")));

        verify(maintenanceService).getMaintenancesByCrewId("A");
    }

    // ---- POST /api/maintenances ----

    @Test
    void createMaintenance_shouldReturnCreated() throws Exception {
        when(maintenanceService.createMaintenance(any(MaintenanceRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/maintenances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.maintenanceId", is("MNT001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/maintenances/1")))
                .andExpect(jsonPath("$._links.all-maintenances.href", containsString("/api/maintenances")));

        verify(maintenanceService).createMaintenance(any(MaintenanceRequestDTO.class));
    }

    // ---- PUT /api/maintenances/{id} ----

    @Test
    void updateMaintenance_shouldReturnUpdated() throws Exception {
        when(maintenanceService.updateMaintenance(eq(1L), any(MaintenanceRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/maintenances/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.maintenanceId", is("MNT001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/maintenances/1")))
                .andExpect(jsonPath("$._links.all-maintenances.href", containsString("/api/maintenances")));

        verify(maintenanceService).updateMaintenance(eq(1L), any(MaintenanceRequestDTO.class));
    }

    // ---- DELETE /api/maintenances/{id} ----

    @Test
    void deleteMaintenance_shouldReturnNoContent() throws Exception {
        doNothing().when(maintenanceService).deleteMaintenance(1L);

        mockMvc.perform(delete("/api/maintenances/1"))
                .andExpect(status().isNoContent());

        verify(maintenanceService).deleteMaintenance(1L);
    }
}