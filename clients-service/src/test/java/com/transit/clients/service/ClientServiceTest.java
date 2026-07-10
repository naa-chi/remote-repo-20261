package com.transit.clients.service;

import com.transit.clients.dto.mapper.ClientMapper;
import com.transit.clients.dto.request.ClientRequestDTO;
import com.transit.clients.dto.response.ClientResponseDTO;
import com.transit.clients.fallback.ClientServiceFallback;
import com.transit.clients.model.ClientModel;
import com.transit.clients.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private ClientMapper mapper;

    @Mock
    private ClientServiceFallback fallback;

    @InjectMocks
    private ClientService clientService;

    private ClientModel model;
    private ClientResponseDTO responseDTO;
    private ClientRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Date registrationDate = Date.valueOf("2024-01-01");

        model = new ClientModel();
        model.setId(1L);
        model.setCode("CLI001");
        model.setFirstName("John");
        model.setLastName("Doe");
        model.setEmail("john.doe@example.com");
        model.setPhoneNumber("1234567890");
        model.setRegistrationDate(registrationDate);

        responseDTO = new ClientResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCode("CLI001");
        responseDTO.setFirstName("John");
        responseDTO.setLastName("Doe");
        responseDTO.setEmail("john.doe@example.com");
        responseDTO.setPhoneNumber("1234567890");
        responseDTO.setRegistrationDate(registrationDate);

        requestDTO = new ClientRequestDTO();
        requestDTO.setCode("CLI001");
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setEmail("john.doe@example.com");
        requestDTO.setPhoneNumber("1234567890");
        requestDTO.setRegistrationDate(registrationDate);
    }

    // ---- getClientById ----

    @Test
    void getClientById_shouldReturnClient_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ClientResponseDTO result = clientService.getClientById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(model);
    }

    @Test
    void getClientById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getClientById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 99");
    }

    // ---- getClientByCode ----

    @Test
    void getClientByCode_shouldReturnClient_whenExists() {
        when(repository.findByCode("CLI001")).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ClientResponseDTO result = clientService.getClientByCode("CLI001");

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findByCode("CLI001");
        verify(mapper).toResponse(model);
    }

    @Test
    void getClientByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getClientByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with code: UNKNOWN");
    }

    // ---- createClient ----

    @Test
    void createClient_shouldSaveAndReturn() {
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ClientResponseDTO result = clientService.createClient(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(model);
        verify(mapper).toResponse(model);
    }

    // ---- updateClient ----

    @Test
    void updateClient_shouldUpdateAndReturn_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        when(mapper.toEntity(requestDTO)).thenReturn(model);
        when(repository.save(any(ClientModel.class))).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        ClientResponseDTO result = clientService.updateClient(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(any(ClientModel.class));
    }

    @Test
    void updateClient_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.updateClient(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 99");
    }

    // ---- deleteClient ----

    @Test
    void deleteClient_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        clientService.deleteClient(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteClient_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> clientService.deleteClient(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found with id: 99");
    }

    // ---- getAllClients ----

    @Test
    void getAllClients_shouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDTO);

        List<ClientResponseDTO> result = clientService.getAllClients();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(model);
    }

    // ========== FALLBACK TESTS ==========
    // Fallback methods are public, so we can call them directly.

    @Test
    void handleGetClientFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getClientFallback(1L, t)).thenReturn(responseDTO);

        ClientResponseDTO result = clientService.handleGetClientFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getClientFallback(1L, t);
    }

    @Test
    void handleGetClientByCodeFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getClientFallback("CLI001", t)).thenReturn(responseDTO);

        ClientResponseDTO result = clientService.handleGetClientByCodeFallback("CLI001", t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getClientFallback("CLI001", t);
    }

    @Test
    void handleGetClientsFallbackList_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<ClientResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getClientsFallbackList(t)).thenReturn(fallbackList);

        List<ClientResponseDTO> result = clientService.handleGetClientsFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getClientsFallbackList(t);
    }
}