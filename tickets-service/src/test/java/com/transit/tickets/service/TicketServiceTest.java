package com.transit.tickets.service;

import com.transit.tickets.dto.mapper.TicketMapper;
import com.transit.tickets.dto.request.TicketRequestDTO;
import com.transit.tickets.dto.response.TicketResponseDTO;
import com.transit.tickets.fallback.TicketServiceFallback;
import com.transit.tickets.model.TicketModel;
import com.transit.tickets.repository.TicketsRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketsRepository repository;

    @Mock
    private TicketMapper mapper;

    @Mock
    private TicketServiceFallback fallback;

    @InjectMocks
    private TicketService ticketService;

    private TicketModel ticketModel;
    private TicketResponseDTO responseDTO;
    private TicketRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        ticketModel = new TicketModel();
        ticketModel.setId(1L);
        ticketModel.setCode("TICKET001");
        ticketModel.setCityCodeOrigin("LON");
        ticketModel.setCityCodeDestination("PAR");
        ticketModel.setPrice(150.0);
        ticketModel.setClientId(100L);
        ticketModel.setTrainId(200L);
        ticketModel.setDepartureDate(Date.valueOf("2023-12-01"));

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

    // ---- getAllTickets ----

    @Test
    void getAllTickets_shouldReturnAllTickets() {
        when(repository.findAll()).thenReturn(List.of(ticketModel));
        when(mapper.toResponse(ticketModel)).thenReturn(responseDTO);

        List<TicketResponseDTO> result = ticketService.getAllTickets();

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findAll();
        verify(mapper).toResponse(ticketModel);
    }

    // ---- getTicketById ----

    @Test
    void getTicketById_shouldReturnTicket_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(ticketModel));
        when(mapper.toResponse(ticketModel)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.getTicketById(1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(mapper).toResponse(ticketModel);
    }

    @Test
    void getTicketById_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket not found: 99");
    }

    // ---- getTicketByCode ----

    @Test
    void getTicketByCode_shouldReturnTicket_whenExists() {
        when(repository.findByCode("TICKET001")).thenReturn(Optional.of(ticketModel));
        when(mapper.toResponse(ticketModel)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.getTicketByCode("TICKET001");

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findByCode("TICKET001");
        verify(mapper).toResponse(ticketModel);
    }

    @Test
    void getTicketByCode_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket not found with code: UNKNOWN");
    }

    // ---- getTicketsByCityCodeOrigin ----

    @Test
    void getTicketsByCityCodeOrigin_shouldReturnList() {
        when(repository.findByCityCodeOrigin("LON")).thenReturn(List.of(ticketModel));
        when(mapper.toResponse(ticketModel)).thenReturn(responseDTO);

        List<TicketResponseDTO> result = ticketService.getTicketsByCityCodeOrigin("LON");

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByCityCodeOrigin("LON");
        verify(mapper).toResponse(ticketModel);
    }

    // ---- getTicketsByCityCodeDestination ----

    @Test
    void getTicketsByCityCodeDestination_shouldReturnList() {
        when(repository.findByCityCodeDestination("PAR")).thenReturn(List.of(ticketModel));
        when(mapper.toResponse(ticketModel)).thenReturn(responseDTO);

        List<TicketResponseDTO> result = ticketService.getTicketsByCityCodeDestination("PAR");

        assertThat(result).hasSize(1).containsExactly(responseDTO);
        verify(repository).findByCityCodeDestination("PAR");
        verify(mapper).toResponse(ticketModel);
    }

    // ---- createTicket ----

    @Test
    void createTicket_shouldSaveAndReturnTicket() {
        when(mapper.toEntity(requestDTO)).thenReturn(ticketModel);
        when(repository.save(ticketModel)).thenReturn(ticketModel);
        when(mapper.toResponse(ticketModel)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.createTicket(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(ticketModel);
        verify(mapper).toResponse(ticketModel);
    }

    // ---- updateTicket ----

    @Test
    void updateTicket_shouldUpdateAndReturn_whenExists() {
        TicketModel existing = new TicketModel();
        existing.setId(1L);
        // The update method mutates the existing entity
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(TicketModel.class))).thenReturn(existing);
        when(mapper.toResponse(any(TicketModel.class))).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.updateTicket(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).findById(1L);
        verify(repository).save(existing);
        // verify that fields were updated
        assertThat(existing.getCode()).isEqualTo(requestDTO.getCode());
        assertThat(existing.getCityCodeOrigin()).isEqualTo(requestDTO.getCityCodeOrigin());
        assertThat(existing.getCityCodeDestination()).isEqualTo(requestDTO.getCityCodeDestination());
        assertThat(existing.getPrice()).isEqualTo(requestDTO.getPrice());
        assertThat(existing.getClientId()).isEqualTo(requestDTO.getClientId());
        assertThat(existing.getTrainId()).isEqualTo(requestDTO.getTrainId());
        assertThat(existing.getDepartureDate()).isEqualTo(requestDTO.getDepartureDate());
    }

    @Test
    void updateTicket_shouldThrowRuntimeException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.updateTicket(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket not found: 99");
    }

    // ---- deleteTicket ----

    @Test
    void deleteTicket_shouldDelete_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        ticketService.deleteTicket(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteTicket_shouldThrowRuntimeException_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> ticketService.deleteTicket(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket not found: 99");
    }

    // ========== FALLBACK TESTS ==========

    // handleGetTicketFallback(Long id, Throwable t)
    @Test
    void handleGetTicketFallback_withId_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getTicketFallback(1L, t)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.handleGetTicketFallback(1L, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getTicketFallback(1L, t);
    }

    // handleGetTicketByCodeFallback(String code, Throwable t)
    @Test
    void handleGetTicketByCodeFallback_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getTicketByCodeFallback("CODE", t)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.handleGetTicketByCodeFallback("CODE", t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getTicketByCodeFallback("CODE", t);
    }

    // handleGetTicketsFallbackList(Throwable t)
    @Test
    void handleGetTicketsFallbackList_withThrowable_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<TicketResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getTicketsFallbackList(t)).thenReturn(fallbackList);

        List<TicketResponseDTO> result = ticketService.handleGetTicketsFallbackList(t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getTicketsFallbackList(t);
    }

    // handleGetTicketsFallbackList(String param, Throwable t)
    @Test
    void handleGetTicketsFallbackList_withStringParam_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        List<TicketResponseDTO> fallbackList = List.of(responseDTO);
        when(fallback.getTicketsFallbackList(t)).thenReturn(fallbackList);

        List<TicketResponseDTO> result = ticketService.handleGetTicketsFallbackList("LON", t);

        assertThat(result).isEqualTo(fallbackList);
        verify(fallback).getTicketsFallbackList(t);
    }

    // handleGetTicketFallback(TicketRequestDTO request, Throwable t)
    @Test
    void handleGetTicketFallback_withRequestDTO_shouldDelegateToFallbackWithMinusOne() {
        Throwable t = new RuntimeException("test");
        when(fallback.getTicketFallback(-1L, t)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.handleGetTicketFallback(requestDTO, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getTicketFallback(-1L, t);
    }

    // handleGetTicketFallback(Long id, TicketRequestDTO request, Throwable t)
    @Test
    void handleGetTicketFallback_withIdAndRequest_shouldDelegateToFallback() {
        Throwable t = new RuntimeException("test");
        when(fallback.getTicketFallback(1L, t)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.handleGetTicketFallback(1L, requestDTO, t);

        assertThat(result).isEqualTo(responseDTO);
        verify(fallback).getTicketFallback(1L, t);
    }
}