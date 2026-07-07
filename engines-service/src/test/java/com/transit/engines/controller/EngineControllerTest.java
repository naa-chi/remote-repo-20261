package com.transit.engines.controller;

import com.transit.engines.dto.EngineDTO;
import com.transit.engines.service.EngineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EngineController.class)
public class EngineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EngineService engineService;

    @Test
    void createEngine_ShouldReturn200() throws Exception {
        // Arrange: Provide all required fields to satisfy @NotNull
        String jsonPayload = """
            {
                "engineCode": "V8",
                "manufacturerId": "M123",
                "productionDate": "2026-07-05",
                "enginePrice": 1500.0,
                "engineWeight": 250.0,
                "engineHorsepower": 450.0
            }
            """;

        when(engineService.createEngine(any(EngineDTO.class))).thenReturn(new EngineDTO());

        // Act & Assert
        mockMvc.perform(post("/engines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk());
    }
}