package com.transit.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.tickets.dto.request.TicketRequestDTO;
import com.transit.tickets.dto.response.TicketResponseDTO;
import com.transit.tickets.service.TicketService;
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

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TicketService ticketService;

    private TicketResponseDTO responseDTO;
    private TicketRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new TicketResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCode("TICKET001");
        responseDTO.setCityCodeOrigin("LON");
        responseDTO.setCityCodeDestination("PAR");
        responseDTO.setPrice(150.0);
        responseDTO.setClientId(100L);
        responseDTO.setTrainId(200L);
        responseDTO.setDepartureDate(Date.valueOf("2023-12-01"));

        requestDTO = new TicketRequestDTO();
        requestDTO.setCode("TICKET001");
        requestDTO.setCityCodeOrigin("LON");
        requestDTO.setCityCodeDestination("PAR");
        requestDTO.setPrice(150.0);
        requestDTO.setClientId(100L);
        requestDTO.setTrainId(200L);
        requestDTO.setDepartureDate(Date.valueOf("2023-12-01"));
    }

    // ---- GET /api/tickets ----

    @Test
    void getAllTickets_shouldReturnList() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/tickets")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0].code", is("TICKET001")))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0]._links.self.href", containsString("/api/tickets/1")))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0]._links.all-tickets.href", containsString("/api/tickets")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/tickets")));

        verify(ticketService).getAllTickets();
    }

    // ---- GET /api/tickets/{id} ----

    @Test
    void getTicketById_shouldReturnTicket_whenExists() throws Exception {
        when(ticketService.getTicketById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/tickets/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("TICKET001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/tickets/1")))
                .andExpect(jsonPath("$._links.all-tickets.href", containsString("/api/tickets")));

        verify(ticketService).getTicketById(1L);
    }

    // ---- GET /api/tickets/code/{code} ----

    @Test
    void getTicketByCode_shouldReturnTicket_whenExists() throws Exception {
        when(ticketService.getTicketByCode("TICKET001")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/tickets/code/TICKET001")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("TICKET001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/tickets/1")))
                .andExpect(jsonPath("$._links.all-tickets.href", containsString("/api/tickets")));

        verify(ticketService).getTicketByCode("TICKET001");
    }

    // ---- GET /api/tickets/origin/{cityCodeOrigin} ----

    @Test
    void getTicketsByCityCodeOrigin_shouldReturnList() throws Exception {
        when(ticketService.getTicketsByCityCodeOrigin("LON")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/tickets/origin/LON")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0]._links.self.href", containsString("/api/tickets/1")))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0]._links.all-tickets.href", containsString("/api/tickets")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/tickets/origin/LON")));

        verify(ticketService).getTicketsByCityCodeOrigin("LON");
    }

    // ---- GET /api/tickets/destination/{cityCodeDestination} ----

    @Test
    void getTicketsByCityCodeDestination_shouldReturnList() throws Exception {
        when(ticketService.getTicketsByCityCodeDestination("PAR")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/tickets/destination/PAR")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0]._links.self.href", containsString("/api/tickets/1")))
                .andExpect(jsonPath("$._embedded.ticketResponseDTOList[0]._links.all-tickets.href", containsString("/api/tickets")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/tickets/destination/PAR")));

        verify(ticketService).getTicketsByCityCodeDestination("PAR");
    }

    // ---- POST /api/tickets ----

    @Test
    void createTicket_shouldReturnCreatedTicket() throws Exception {
        when(ticketService.createTicket(any(TicketRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("TICKET001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/tickets/1")))
                .andExpect(jsonPath("$._links.all-tickets.href", containsString("/api/tickets")));

        verify(ticketService).createTicket(any(TicketRequestDTO.class));
    }

    // ---- PUT /api/tickets/{id} ----

    @Test
    void updateTicket_shouldReturnUpdatedTicket() throws Exception {
        when(ticketService.updateTicket(eq(1L), any(TicketRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("TICKET001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/tickets/1")))
                .andExpect(jsonPath("$._links.all-tickets.href", containsString("/api/tickets")));

        verify(ticketService).updateTicket(eq(1L), any(TicketRequestDTO.class));
    }

    // ---- DELETE /api/tickets/{id} ----

    @Test
    void deleteTicket_shouldReturnNoContent() throws Exception {
        doNothing().when(ticketService).deleteTicket(1L);

        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isNoContent());

        verify(ticketService).deleteTicket(1L);
    }
}