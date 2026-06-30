package com.gTransitProject.maintenance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gTransitProject.maintenance.model.maintenanceModel;
import com.gTransitProject.maintenance.service.maintenanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(maintenanceController.class)
class maintenanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private maintenanceService service;

    private maintenanceModel sampleRecord;

    @BeforeEach
    void setUp() {
        sampleRecord = new maintenanceModel();
        sampleRecord.setId(1);
        sampleRecord.setEntry_date_of_maintenance(Date.valueOf("2026-06-01"));
        sampleRecord.setLeave_date_of_maintenance(Date.valueOf("2026-06-05"));
        sampleRecord.setIssue_description("Engine overheating issues");
        sampleRecord.setReport_description("Replaced coolant pump and flushed system");
        sampleRecord.setMaintenance_price(450);
        sampleRecord.setVehicleId(1024);
        sampleRecord.setModel("Train-X200");
        sampleRecord.setRisk_importance(3);
        sampleRecord.setResponsible_maintanance_crew(12);
    }

    @Test
    void findAll_ShouldReturnCollectionWithEmbeddedRecords() throws Exception {
        List<maintenanceModel> allRecords = Arrays.asList(sampleRecord);
        when(service.getAllMaintenance()).thenReturn(allRecords);

        mockMvc.perform(get("/api/maintenances")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.maintenanceModel[0].id").value(1))
                .andExpect(jsonPath("$._embedded.maintenanceModel[0].model").value("Train-X200"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void findById_ShouldReturnRecordWithLinks() throws Exception {
        when(service.getMaintenanceById(1)).thenReturn(sampleRecord);

        mockMvc.perform(get("/api/maintenances/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.model").value("Train-X200"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.allMaintenances.href").exists());
    }

    @Test
    void findById_NotFound_ShouldReturn404() throws Exception {
        when(service.getMaintenanceById(999)).thenReturn(null);

        mockMvc.perform(get("/api/maintenances/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByVehicleId_ShouldReturnRecordWithLinks() throws Exception {
        when(service.getMaintenanceByVehicleId(1024)).thenReturn(sampleRecord);

        mockMvc.perform(get("/api/maintenances/vehicle/1024")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.vehicleId").value(1024))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void findByVehicleId_NotFound_ShouldReturn404() throws Exception {
        when(service.getMaintenanceByVehicleId(9999)).thenReturn(null);

        mockMvc.perform(get("/api/maintenances/vehicle/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void save_ShouldCreateAndReturnRecordWithLinks() throws Exception {
        when(service.createMaintenance(any(maintenanceModel.class))).thenReturn(sampleRecord);

        mockMvc.perform(post("/api/maintenances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.model").value("Train-X200"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void update_ShouldModifyAndReturnRecordWithLinks() throws Exception {
        when(service.updateMaintenance(eq(1), any(maintenanceModel.class))).thenReturn(sampleRecord);

        mockMvc.perform(put("/api/maintenances/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.risk_importance").value(3))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void update_NotFound_ShouldReturn404() throws Exception {
        when(service.updateMaintenance(eq(999), any(maintenanceModel.class))).thenReturn(null);

        mockMvc.perform(put("/api/maintenances/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRecord)))
                .andExpect(status().isNotFound());
    }
}