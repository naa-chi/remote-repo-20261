package com.transit.trains.controller;

import com.transit.trains.dto.TrainDTO;
import com.transit.trains.service.TrainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=" +
    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
    "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
    "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
public class TrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainService trainService;

    @Test
    void createTrain_ShouldReturn200_WhenValid() throws Exception {
        String jsonPayload = """
            {
                "code": "T100",
                "manufacturerId": "M123",
                "engineId": 5,
                "carAmount": 10,
                "costPerCar": 5000,
                "productionDate": "2026-07-05"
            }
            """;

        TrainDTO mockResponse = new TrainDTO();
        mockResponse.setId(1L);
        when(trainService.createTrain(any(TrainDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/trains/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk());
    }

    @Test
    void createTrain_ShouldReturn400_WhenInvalid() throws Exception {
        String jsonPayload = """
            {
                "manufacturerId": "M123"
            }
            """;

        mockMvc.perform(post("/api/trains/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTrainById_ShouldReturn200_WhenExists() throws Exception {
        Long id = 1L;
        TrainDTO mockTrain = new TrainDTO();
        mockTrain.setId(id);
        mockTrain.setCode("T100");
        when(trainService.getTrainById(id)).thenReturn(mockTrain);

        mockMvc.perform(get("/api/trains/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void deleteTrain_ShouldReturn204() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/trains/{id}", id))
                .andExpect(status().isNoContent());
    }
}