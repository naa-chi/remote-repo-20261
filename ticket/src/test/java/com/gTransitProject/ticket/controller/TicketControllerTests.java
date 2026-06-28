package com.gTransitProject.ticket.controller;

import com.gTransitProject.ticket.model.ticketModel;
import com.gTransitProject.ticket.service.ticketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ticketControllerTest {

    @Mock
    private ticketService service;

    @InjectMocks
    private ticketController controller;

    private ticketModel sampleTicket;

    @BeforeEach
    void setUp() {
        sampleTicket = new ticketModel();
        sampleTicket.setTicketId(1);
        sampleTicket.setCode("LANY001");
        sampleTicket.setPrice(150);
        sampleTicket.setCityCodeDestination("NYC");
        sampleTicket.setUniqueStationCodeDestination("NYC001");
        sampleTicket.setCityCodeOrigin("LAX");
        sampleTicket.setUniqueStationCodeOrigin("LAX001");
        sampleTicket.setLineNumber(7);
        sampleTicket.setTrainCode("TR123");
        sampleTicket.setClientId(1001);
        sampleTicket.setDriverId("D123");
        sampleTicket.setDepartureTime(Date.valueOf("2026-06-26"));
        sampleTicket.setArrivalTime(Date.valueOf("2026-06-27"));
        sampleTicket.setKgCargo(500);
    }

    @Test
    void getTickets_shouldReturnListOfTickets() {
        List<ticketModel> tickets = Arrays.asList(sampleTicket, new ticketModel());
        when(service.getAll()).thenReturn(tickets);

        List<ticketModel> result = controller.getTickets();

        assertEquals(2, result.size());
        verify(service, times(1)).getAll();
    }

    @Test
    void getTicketById_withValidId_shouldReturnTicket() {
        Integer id = 1;
        when(service.getById(id)).thenReturn(sampleTicket);

        ticketModel result = controller.getTicketById(id);

        assertEquals(sampleTicket, result);
        verify(service, times(1)).getById(id);
    }

    @Test
    void getTicketById_withInvalidId_shouldReturnNull() {
        Integer id = 999;
        when(service.getById(id)).thenReturn(null);

        ticketModel result = controller.getTicketById(id);

        assertNull(result);
        verify(service, times(1)).getById(id);
    }

    @Test
    void getTicketByCode_withValidCode_shouldReturnTicket() {
        String code = "LANY001";
        when(service.getByCode(code)).thenReturn(sampleTicket);

        ticketModel result = controller.getTicketByCode(code);

        assertEquals(sampleTicket, result);
        verify(service, times(1)).getByCode(code);
    }

    @Test
    void getTicketByCode_withInvalidCode_shouldReturnNull() {
        String code = "INVALID";
        when(service.getByCode(code)).thenReturn(null);

        ticketModel result = controller.getTicketByCode(code);

        assertNull(result);
        verify(service, times(1)).getByCode(code);
    }

    @Test
    void getTicketByCityCodeOrigin_withValidOrigin_shouldReturnTicket() {
        String origin = "LAX";
        when(service.getCityCodeOrigin(origin)).thenReturn(sampleTicket);

        ticketModel result = controller.getTicketByCityCodeOrigin(origin);

        assertEquals(sampleTicket, result);
        verify(service, times(1)).getCityCodeOrigin(origin);
    }

    @Test
    void getTicketByCityCodeOrigin_withInvalidOrigin_shouldReturnNull() {
        String origin = "XXX";
        when(service.getCityCodeOrigin(origin)).thenReturn(null);

        ticketModel result = controller.getTicketByCityCodeOrigin(origin);

        assertNull(result);
        verify(service, times(1)).getCityCodeOrigin(origin);
    }

    @Test
    void getTicketByCityCodeDestination_withValidDestination_shouldReturnTicket() {
        String dest = "NYC";
        when(service.getCityCodeDestination(dest)).thenReturn(sampleTicket);

        ticketModel result = controller.getTicketByCityCodeDestination(dest);

        assertEquals(sampleTicket, result);
        verify(service, times(1)).getCityCodeDestination(dest);
    }

    @Test
    void getTicketByCityCodeDestination_withInvalidDestination_shouldReturnNull() {
        String dest = "XXX";
        when(service.getCityCodeDestination(dest)).thenReturn(null);

        ticketModel result = controller.getTicketByCityCodeDestination(dest);

        assertNull(result);
        verify(service, times(1)).getCityCodeDestination(dest);
    }

    @Test
    void saveTicket_shouldReturnSavedTicket() {
        when(service.create(any(ticketModel.class))).thenReturn(sampleTicket);

        ticketModel result = controller.saveTicket(sampleTicket);

        assertEquals(sampleTicket, result);
        verify(service, times(1)).create(sampleTicket);
    }

    @Test
    void deleteTicket_shouldCallServiceDelete() {
        Integer id = 1;
        doNothing().when(service).delete(id);

        controller.deleteTicket(id);

        verify(service, times(1)).delete(id);
    }

    @Test
    void updateTicket_shouldReturnUpdatedTicket() {
        Integer id = 1;
        ticketModel updated = new ticketModel();
        updated.setTicketId(1);
        updated.setPrice(200);
        when(service.update(eq(id), any(ticketModel.class))).thenReturn(updated);

        ticketModel result = controller.updateTicket(id, sampleTicket);

        assertEquals(updated, result);
        verify(service, times(1)).update(id, sampleTicket);
    }
}