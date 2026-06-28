package com.gTransitProject.supervisor.controller;

import com.gTransitProject.supervisor.model.Supervisor;
import com.gTransitProject.supervisor.service.SupervisorService;
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
class SupervisorControllerTest {

    @Mock
    private SupervisorService supervisorService;

    @InjectMocks
    private SupervisorController controller;

    private Supervisor sampleSupervisor;

    @BeforeEach
    void setUp() {
        sampleSupervisor = new Supervisor();
        sampleSupervisor.setSupervisorId(1);
        sampleSupervisor.setSupervisorName("John Doe");
        sampleSupervisor.setSupervisorCode("ABC123");
        sampleSupervisor.setCityCode("NYC");
        sampleSupervisor.setAuthorized(true);
    }

    @Test
    void getAllSupervisors_shouldReturnListOfSupervisors() {
        List<Supervisor> supervisors = Arrays.asList(sampleSupervisor, new Supervisor());
        when(supervisorService.getAllSupervisors()).thenReturn(supervisors);

        ResponseEntity<List<Supervisor>> response = controller.getAllSupervisors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(supervisorService, times(1)).getAllSupervisors();
    }

    @Test
    void createSupervisor_shouldReturnSavedSupervisor() {
        when(supervisorService.saveSupervisor(any(Supervisor.class))).thenReturn(sampleSupervisor);

        ResponseEntity<Supervisor> response = controller.createSupervisor(sampleSupervisor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleSupervisor, response.getBody());
        verify(supervisorService, times(1)).saveSupervisor(sampleSupervisor);
    }

    @Test
    void validateSupervisor_withValidCode_shouldReturnAuthorizedStatus() {
        String code = "ABC123";
        when(supervisorService.findByCode(code)).thenReturn(sampleSupervisor);

        ResponseEntity<Boolean> response = controller.validateSupervisor(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(supervisorService, times(1)).findByCode(code);
    }

    @Test
    void validateSupervisor_withInvalidCode_shouldThrowNullPointerException() {
        String code = "INVALID";
        when(supervisorService.findByCode(code)).thenReturn(null);

        // The controller does not handle null, so calling getAuthorized() will throw NPE
        assertThrows(NullPointerException.class, () -> controller.validateSupervisor(code));
        verify(supervisorService, times(1)).findByCode(code);
    }

    @Test
    void deleteSupervisor_shouldReturnSuccessMessage() {
        Integer id = 1;
        doNothing().when(supervisorService).deleteSupervisor(id);

        ResponseEntity<String> response = controller.deleteSupervisor(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Supervisor eliminado", response.getBody());
        verify(supervisorService, times(1)).deleteSupervisor(id);
    }

    @Test
    void getSupervisorById_withValidId_shouldReturnSupervisor() {
        Integer id = 1;
        when(supervisorService.getSupervisorById(id)).thenReturn(sampleSupervisor);

        ResponseEntity<Supervisor> response = controller.getSupervisorById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleSupervisor, response.getBody());
        verify(supervisorService, times(1)).getSupervisorById(id);
    }

    @Test
    void getSupervisorById_withInvalidId_shouldReturnNull() {
        Integer id = 999;
        when(supervisorService.getSupervisorById(id)).thenReturn(null);

        ResponseEntity<Supervisor> response = controller.getSupervisorById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(supervisorService, times(1)).getSupervisorById(id);
    }

    @Test
    void updateSupervisor_shouldReturnUpdatedSupervisor() {
        Integer id = 1;
        Supervisor updated = new Supervisor();
        updated.setSupervisorName("Jane Doe");
        when(supervisorService.updateSupervisor(eq(id), any(Supervisor.class))).thenReturn(updated);

        ResponseEntity<Supervisor> response = controller.updateSupervisor(id, sampleSupervisor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(supervisorService, times(1)).updateSupervisor(id, sampleSupervisor);
    }
}