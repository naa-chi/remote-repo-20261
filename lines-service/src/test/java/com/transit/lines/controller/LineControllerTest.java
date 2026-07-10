package com.transit.lines.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.lines.dto.request.LineRequestDTO;
import com.transit.lines.dto.response.LineResponseDTO;
import com.transit.lines.service.LineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LineService lineService;

    private LineResponseDTO responseDTO;
    private LineRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new LineResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setLineCode(10);
        responseDTO.setLineLengthKM(42L);
        responseDTO.setPeopleServedMonthlyEstimate(500000L);

        requestDTO = new LineRequestDTO();
        requestDTO.setLineCode(10);
        requestDTO.setLineLengthKM(42L);
        requestDTO.setPeopleServedMonthlyEstimate(500000L);
    }

    // ---- GET /api/lines ----

    @Test
    void getAllLines_shouldReturnList() throws Exception {
        when(lineService.getAllLines()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/lines")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.lineResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.lineResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.lineResponseDTOList[0].lineCode", is(10)))
                .andExpect(jsonPath("$._embedded.lineResponseDTOList[0].lineLengthKM", is(42)))
                .andExpect(jsonPath("$._embedded.lineResponseDTOList[0]._links.self.href", containsString("/api/lines/1")))
                .andExpect(jsonPath("$._embedded.lineResponseDTOList[0]._links.all-lines.href", containsString("/api/lines")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/lines")));

        verify(lineService).getAllLines();
    }

    // ---- GET /api/lines/{id} ----

    @Test
    void getLineById_shouldReturnLine() throws Exception {
        when(lineService.getLineById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/lines/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.lineCode", is(10)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/lines/1")))
                .andExpect(jsonPath("$._links.all-lines.href", containsString("/api/lines")));

        verify(lineService).getLineById(1L);
    }

    // ---- GET /api/lines/code/{lineCode} ----

    @Test
    void getLineByCode_shouldReturnLine() throws Exception {
        when(lineService.getLineByCode(10)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/lines/code/10")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.lineCode", is(10)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/lines/1")))
                .andExpect(jsonPath("$._links.all-lines.href", containsString("/api/lines")));

        verify(lineService).getLineByCode(10);
    }

    // ---- POST /api/lines ----

    @Test
    void createLine_shouldReturnCreated() throws Exception {
        when(lineService.createLine(any(LineRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.lineCode", is(10)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/lines/1")))
                .andExpect(jsonPath("$._links.all-lines.href", containsString("/api/lines")));

        verify(lineService).createLine(any(LineRequestDTO.class));
    }

    // ---- PUT /api/lines/{id} ----

    @Test
    void updateLine_shouldReturnUpdated() throws Exception {
        when(lineService.updateLine(eq(1L), any(LineRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/lines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.lineCode", is(10)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/lines/1")))
                .andExpect(jsonPath("$._links.all-lines.href", containsString("/api/lines")));

        verify(lineService).updateLine(eq(1L), any(LineRequestDTO.class));
    }

    // ---- DELETE /api/lines/{id} ----

    @Test
    void deleteLine_shouldReturnNoContent() throws Exception {
        doNothing().when(lineService).deleteLine(1L);

        mockMvc.perform(delete("/api/lines/1"))
                .andExpect(status().isNoContent());

        verify(lineService).deleteLine(1L);
    }
}