package com.gTransitProject.ticket.controller;

import com.gTransitProject.ticket.model.ticketModel;
import com.gTransitProject.ticket.service.ticketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ticketControllerTests {

    @Mock
    private ticketService service;

    @Mock
    private ticketAssembler assembler;

    @InjectMocks
    private ticketController controller;

    private ticketModel sampleTicket;
    private EntityModel<ticketModel> sampleModel;

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

        sampleModel = EntityModel.of(sampleTicket);
    }

    @Test
    void getTickets_shouldReturnCollectionModelWithSelfLink() {
        List<ticketModel> tickets = Arrays.asList(sampleTicket, new ticketModel());
        CollectionModel<EntityModel<ticketModel>> collection = CollectionModel.of(
                Arrays.asList(EntityModel.of(tickets.get(0)), EntityModel.of(tickets.get(1))));
        when(service.getAll()).thenReturn(tickets);
        when(assembler.toCollectionModel(tickets)).thenReturn(collection);

        CollectionModel<EntityModel<ticketModel>> result = controller.getTickets();

        assertEquals(2, result.getContent().size());
        verify(service, times(1)).getAll();
        verify(assembler, times(1)).toCollectionModel(tickets);
    }

    @Test
    void getTicketById_withValidId_shouldReturnOkResponseWithModel() {
        Integer id = 1;
        when(service.getById(id)).thenReturn(sampleTicket);
        when(assembler.toModel(sampleTicket)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<ticketModel>> response = controller.getTicketById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(service, times(1)).getById(id);
        verify(assembler, times(1)).toModel(sampleTicket);
    }

    @Test
    void getTicketById_withInvalidId_shouldReturnNotFound() {
        Integer id = 999;
        when(service.getById(id)).thenReturn(null);

        ResponseEntity<EntityModel<ticketModel>> response = controller.getTicketById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).getById(id);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void getTicketByCode_withValidCode_shouldReturnOkResponseWithModel() {
        String code = "LANY001";
        when(service.getByCode(code)).thenReturn(sampleTicket);
        when(assembler.toModel(sampleTicket)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<ticketModel>> response = controller.getTicketByCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(service, times(1)).getByCode(code);
        verify(assembler, times(1)).toModel(sampleTicket);
    }

    @Test
    void getTicketByCode_withInvalidCode_shouldReturnNotFound() {
        String code = "INVALID";
        when(service.getByCode(code)).thenReturn(null);

        ResponseEntity<EntityModel<ticketModel>> response = controller.getTicketByCode(code);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).getByCode(code);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void getTicketByCityCodeOrigin_withValidOrigin_shouldReturnOkResponseWithModel() {
        String origin = "LAX";
        when(service.getCityCodeOrigin(origin)).thenReturn(sampleTicket);
        when(assembler.toModel(sampleTicket)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<ticketModel>> response = controller.getTicketByCityCodeOrigin(origin);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(service, times(1)).getCityCodeOrigin(origin);
        verify(assembler, times(1)).toModel(sampleTicket);
    }

    @Test
    void getTicketByCityCodeOrigin_withInvalidOrigin_shouldReturnNotFound() {
        String origin = "XXX";
        when(service.getCityCodeOrigin(origin)).thenReturn(null);

        ResponseEntity<EntityModel<ticketModel>> response = controller.getTicketByCityCodeOrigin(origin);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).getCityCodeOrigin(origin);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void getTicketByCityCodeDestination_withValidDestination_shouldReturnOkResponseWithModel() {
        String dest = "NYC";
        when(service.getCityCodeDestination(dest)).thenReturn(sampleTicket);
        when(assembler.toModel(sampleTicket)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<ticketModel>> response = controller.getTicketByCityCodeDestination(dest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(service, times(1)).getCityCodeDestination(dest);
        verify(assembler, times(1)).toModel(sampleTicket);
    }

    @Test
    void getTicketByCityCodeDestination_withInvalidDestination_shouldReturnNotFound() {
        String dest = "XXX";
        when(service.getCityCodeDestination(dest)).thenReturn(null);

        ResponseEntity<EntityModel<ticketModel>> response = controller.getTicketByCityCodeDestination(dest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).getCityCodeDestination(dest);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void saveTicket_shouldReturnCreatedResponseWithModel() {
        when(service.create(any(ticketModel.class))).thenReturn(sampleTicket);
        when(assembler.toModel(sampleTicket)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<ticketModel>> response = controller.saveTicket(sampleTicket);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(service, times(1)).create(sampleTicket);
        verify(assembler, times(1)).toModel(sampleTicket);
    }

    @Test
    void deleteTicket_shouldReturnNoContent() {
        Integer id = 1;
        doNothing().when(service).delete(id);

        ResponseEntity<Void> response = controller.deleteTicket(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).delete(id);
    }

    @Test
    void updateTicket_shouldReturnOkResponseWithUpdatedModel() {
        Integer id = 1;
        ticketModel updated = new ticketModel();
        updated.setTicketId(1);
        updated.setPrice(200);
        when(service.update(eq(id), any(ticketModel.class))).thenReturn(updated);
        when(assembler.toModel(updated)).thenReturn(EntityModel.of(updated));

        ResponseEntity<EntityModel<ticketModel>> response = controller.updateTicket(id, sampleTicket);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EntityModel.of(updated), response.getBody());
        verify(service, times(1)).update(id, sampleTicket);
        verify(assembler, times(1)).toModel(updated);
    }
}