package com.gTransitProject.maintenance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gTransitProject.maintenance.controller.maintenanceController;
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

    @MockitoBean // Fully supported in Spring Boot 3.4+
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
    void findAll_ShouldReturnListOfRecords() throws Exception {
        List<maintenanceModel> allRecords = Arrays.asList(sampleRecord);
        when(service.getAllMaintenance()).thenReturn(allRecords);

        mockMvc.perform(get("/api/maintenance")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].model").value("Train-X200"));
    }

    @Test
    void findById_ShouldReturnSingleRecord() throws Exception {
        when(service.getMaintenanceById(1)).thenReturn(sampleRecord);

        mockMvc.perform(get("/api/maintenance/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}