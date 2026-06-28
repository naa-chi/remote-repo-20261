package com.gTransitProject.manufacturer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gTransitProject.manufacturer.model.manufacturerModel;
import com.gTransitProject.manufacturer.service.manufacturerService;
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


@WebMvcTest(manufacturerController.class)
class manufacturerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Serializes Java objects into JSON strings

    @MockitoBean
    private manufacturerService service;

    private manufacturerModel sampleManufacturer;

    @BeforeEach
    void setUp() {
        // Initialize a clean, valid test record matching your model parameters
        sampleManufacturer = new manufacturerModel();
        sampleManufacturer.setId(1);
        sampleManufacturer.setName("Bombardier Transportation");
        sampleManufacturer.setCountry("Canada");
    }

    @Test
    void findAll_ShouldReturnListOfManufacturers() throws Exception {
        List<manufacturerModel> list = Arrays.asList(sampleManufacturer);
        when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/manufacturers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Bombardier Transportation"))
                .andExpect(jsonPath("$[0].country").value("Canada"));
    }

    @Test
    void findById_ShouldReturnManufacturer() throws Exception {
        when(service.getById(1)).thenReturn(sampleManufacturer);

        mockMvc.perform(get("/api/manufacturers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bombardier Transportation"));
    }

    @Test
    void findByName_ShouldReturnManufacturer() throws Exception {
        when(service.getByName("Bombardier Transportation")).thenReturn(sampleManufacturer);

        mockMvc.perform(get("/api/manufacturers/name/Bombardier Transportation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bombardier Transportation"));
    }

    @Test
    void findByCountry_ShouldReturnListOfManufacturers() throws Exception {
        List<manufacturerModel> list = Arrays.asList(sampleManufacturer);
        when(service.getByCountry("Canada")).thenReturn(list);

        mockMvc.perform(get("/api/manufacturers/country/Canada")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country").value("Canada"))
                .andExpect(jsonPath("$[0].name").value("Bombardier Transportation"));
    }

    @Test
    void save_ShouldCreateAndReturnManufacturer() throws Exception {
        when(service.create(any(manufacturerModel.class))).thenReturn(sampleManufacturer);

        mockMvc.perform(post("/api/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleManufacturer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bombardier Transportation"));
    }

    @Test
    void update_ShouldModifyAndReturnManufacturer() throws Exception {
        when(service.updateManufacturer(eq(1), any(manufacturerModel.class))).thenReturn(sampleManufacturer);

        mockMvc.perform(put("/api/manufacturers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleManufacturer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.country").value("Canada"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        // Use doNothing() for void methods in your service layer
        doNothing().when(service).delete(1);

        mockMvc.perform(delete("/api/manufacturers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Verifies 204 No Content HTTP Status
    }
}