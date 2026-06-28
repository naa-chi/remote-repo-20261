package com.gTransitProject.client.controller;

import com.gTransitProject.client.model.client;
import com.gTransitProject.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class clientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController controller;

    private client sampleClient;

    @BeforeEach
    void setUp() {
        sampleClient = new client();
        sampleClient.setClientId(1);
        sampleClient.setClientName("John Doe");
        sampleClient.setRequestCity("Los Angeles");
        sampleClient.setProviderCity("New York");
        sampleClient.setRequestedResource("Cargo");
        sampleClient.setOfferedReward("5000");
        sampleClient.setAuthCode("AUTH123");
        sampleClient.setStatus("ACTIVE");
    }

    @Test
    void getAllClients_shouldReturnListOfClients() {
        List<client> clients = Arrays.asList(sampleClient, new client());
        when(clientService.getAllClients()).thenReturn(clients);

        ResponseEntity<List<client>> response = controller.getAllClients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void createClient_shouldReturnCreatedClient() {
        when(clientService.saveClient(any(client.class))).thenReturn(sampleClient);

        ResponseEntity<client> response = controller.createClient(sampleClient);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleClient, response.getBody());
        verify(clientService, times(1)).saveClient(sampleClient);
    }

    @Test
    void getClientById_withValidId_shouldReturnClient() {
        Integer id = 1;
        when(clientService.getClientById(id)).thenReturn(sampleClient);

        ResponseEntity<client> response = controller.getClientById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleClient, response.getBody());
        verify(clientService, times(1)).getClientById(id);
    }

    @Test
    void getClientById_withInvalidId_shouldReturnNull() {
        Integer id = 999;
        when(clientService.getClientById(id)).thenReturn(null);

        ResponseEntity<client> response = controller.getClientById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(clientService, times(1)).getClientById(id);
    }

    @Test
    void updateClient_shouldReturnUpdatedClient() {
        Integer id = 1;
        client updated = new client();
        updated.setClientId(1);
        updated.setClientName("Jane Doe");
        when(clientService.updateClient(eq(id), any(client.class))).thenReturn(updated);

        ResponseEntity<client> response = controller.updateClient(id, sampleClient);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(clientService, times(1)).updateClient(id, sampleClient);
    }

    @Test
    void deleteClient_shouldReturnSuccessMessage() {
        Integer id = 1;
        doNothing().when(clientService).deleteClient(id);

        ResponseEntity<String> response = controller.deleteClient(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cliente eliminado", response.getBody());
        verify(clientService, times(1)).deleteClient(id);
    }

    @Test
    void validateClient_withValidCode_shouldReturnStatus() {
        String code = "AUTH123";
        when(clientService.findByAuthCode(code)).thenReturn(sampleClient);

        ResponseEntity<String> response = controller.validateClient(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ACTIVE", response.getBody());
        verify(clientService, times(1)).findByAuthCode(code);
    }

    @Test
    void validateClient_withInvalidCode_shouldThrowNullPointerException() {
        String code = "INVALID";
        when(clientService.findByAuthCode(code)).thenReturn(null);

        // The controller calls clientb.getStatus() without null check → NPE
        assertThrows(NullPointerException.class, () -> controller.validateClient(code));
        verify(clientService, times(1)).findByAuthCode(code);
    }
}