package com.transit.clients.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.clients.dto.request.ClientRequestDTO;
import com.transit.clients.dto.response.ClientResponseDTO;
import com.transit.clients.service.ClientService;
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

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    private ClientResponseDTO responseDTO;
    private ClientRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date registrationDate = Date.valueOf("2024-01-01");
        // Valid code length: 6 (min=3, max=20)
        // Valid phone length: 10 (min=10, max=20)
        String validCode = "CLI001";
        String validPhone = "1234567890";

        responseDTO = new ClientResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCode(validCode);
        responseDTO.setFirstName("John");
        responseDTO.setLastName("Doe");
        responseDTO.setEmail("john.doe@example.com");
        responseDTO.setPhoneNumber(validPhone);
        responseDTO.setRegistrationDate(registrationDate);

        requestDTO = new ClientRequestDTO();
        requestDTO.setCode(validCode);
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setEmail("john.doe@example.com");
        requestDTO.setPhoneNumber(validPhone);
        requestDTO.setRegistrationDate(registrationDate);
    }

    // ---- GET /api/clients ----

    @Test
    void getAllClients_shouldReturnList() throws Exception {
        when(clientService.getAllClients()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/clients")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clientResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.clientResponseDTOList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.clientResponseDTOList[0].code", is("CLI001")))
                .andExpect(jsonPath("$._embedded.clientResponseDTOList[0]._links.self.href", containsString("/api/clients/1")))
                .andExpect(jsonPath("$._embedded.clientResponseDTOList[0]._links.all-clients.href", containsString("/api/clients")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/clients")));

        verify(clientService).getAllClients();
    }

    // ---- GET /api/clients/{id} ----

    @Test
    void getClientById_shouldReturnClient() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/clients/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("CLI001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/clients/1")))
                .andExpect(jsonPath("$._links.all-clients.href", containsString("/api/clients")));

        verify(clientService).getClientById(1L);
    }

    // ---- GET /api/clients/code/{code} ----

    @Test
    void getClientByCode_shouldReturnClient() throws Exception {
        when(clientService.getClientByCode("CLI001")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/clients/code/CLI001")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("CLI001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/clients/1")))
                .andExpect(jsonPath("$._links.all-clients.href", containsString("/api/clients")));

        verify(clientService).getClientByCode("CLI001");
    }

    // ---- POST /api/clients ----

    @Test
    void createClient_shouldReturnCreated() throws Exception {
        when(clientService.createClient(any(ClientRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("CLI001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/clients/1")))
                .andExpect(jsonPath("$._links.all-clients.href", containsString("/api/clients")));

        verify(clientService).createClient(any(ClientRequestDTO.class));
    }

    // ---- PUT /api/clients/{id} ----

    @Test
    void updateClient_shouldReturnUpdated() throws Exception {
        when(clientService.updateClient(eq(1L), any(ClientRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("CLI001")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/clients/1")))
                .andExpect(jsonPath("$._links.all-clients.href", containsString("/api/clients")));

        verify(clientService).updateClient(eq(1L), any(ClientRequestDTO.class));
    }

    // ---- DELETE /api/clients/{id} ----

    @Test
    void deleteClient_shouldReturnNoContent() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());

        verify(clientService).deleteClient(1L);
    }
}