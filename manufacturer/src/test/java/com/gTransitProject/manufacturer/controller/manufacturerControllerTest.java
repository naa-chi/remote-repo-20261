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
    private ObjectMapper objectMapper;

    @MockitoBean
    private manufacturerService service;

    private manufacturerModel sampleManufacturer;

    @BeforeEach
    void setUp() {
        sampleManufacturer = new manufacturerModel();
        sampleManufacturer.setId(1);
        sampleManufacturer.setName("Bombardier Transportation");
        sampleManufacturer.setCountry("Canada");
    }

    @Test
    void findAll_ShouldReturnCollectionWithEmbeddedManufacturers() throws Exception {
        List<manufacturerModel> list = Arrays.asList(sampleManufacturer);
        when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/manufacturers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.manufacturerModel[0].id").value(1))
                .andExpect(jsonPath("$._embedded.manufacturerModel[0].name").value("Bombardier Transportation"))
                .andExpect(jsonPath("$._embedded.manufacturerModel[0].country").value("Canada"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void findById_ShouldReturnManufacturerWithLinks() throws Exception {
        when(service.getById(1)).thenReturn(sampleManufacturer);

        mockMvc.perform(get("/api/manufacturers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bombardier Transportation"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.allManufacturers.href").exists());
    }

    @Test
    void findById_NotFound_ShouldReturn404() throws Exception {
        when(service.getById(999)).thenReturn(null);

        mockMvc.perform(get("/api/manufacturers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByName_ShouldReturnManufacturerWithLinks() throws Exception {
        when(service.getByName("Bombardier Transportation")).thenReturn(sampleManufacturer);

        mockMvc.perform(get("/api/manufacturers/name/Bombardier Transportation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bombardier Transportation"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void findByName_NotFound_ShouldReturn404() throws Exception {
        when(service.getByName("NonExistent")).thenReturn(null);

        mockMvc.perform(get("/api/manufacturers/name/NonExistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByCountry_ShouldReturnCollectionWithEmbeddedManufacturers() throws Exception {
        List<manufacturerModel> list = Arrays.asList(sampleManufacturer);
        when(service.getByCountry("Canada")).thenReturn(list);

        mockMvc.perform(get("/api/manufacturers/country/Canada")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.manufacturerModel[0].country").value("Canada"))
                .andExpect(jsonPath("$._embedded.manufacturerModel[0].name").value("Bombardier Transportation"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void save_ShouldCreateAndReturnManufacturerWithLinks() throws Exception {
        when(service.create(any(manufacturerModel.class))).thenReturn(sampleManufacturer);

        mockMvc.perform(post("/api/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleManufacturer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bombardier Transportation"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void update_ShouldModifyAndReturnManufacturerWithLinks() throws Exception {
        when(service.updateManufacturer(eq(1), any(manufacturerModel.class))).thenReturn(sampleManufacturer);

        mockMvc.perform(put("/api/manufacturers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleManufacturer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.country").value("Canada"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void update_NotFound_ShouldReturn404() throws Exception {
        when(service.updateManufacturer(eq(999), any(manufacturerModel.class))).thenReturn(null);

        mockMvc.perform(put("/api/manufacturers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleManufacturer)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(service).delete(1);

        mockMvc.perform(delete("/api/manufacturers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}