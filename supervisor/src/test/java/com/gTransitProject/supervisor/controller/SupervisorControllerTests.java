package com.gTransitProject.supervisor.controller;

import com.gTransitProject.supervisor.model.Supervisor;
import com.gTransitProject.supervisor.service.SupervisorService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupervisorControllerTest {

    @Mock
    private SupervisorService supervisorService;

    @Mock
    private SupervisorAssembler assembler;

    @InjectMocks
    private SupervisorController controller;

    private Supervisor sampleSupervisor;
    private EntityModel<Supervisor> sampleModel;

    @BeforeEach
    void setUp() {
        sampleSupervisor = new Supervisor();
        sampleSupervisor.setSupervisorId(1);
        sampleSupervisor.setSupervisorName("John Doe");
        sampleSupervisor.setSupervisorCode("ABC123");
        sampleSupervisor.setCityCode("NYC");
        sampleSupervisor.setAuthorized(true);

        sampleModel = EntityModel.of(sampleSupervisor);
    }

    @Test
    void getAllSupervisors_shouldReturnCollectionModelWithSelfLink() {
        List<Supervisor> supervisors = Arrays.asList(sampleSupervisor, new Supervisor());
        CollectionModel<EntityModel<Supervisor>> collection = CollectionModel.of(
                Arrays.asList(EntityModel.of(supervisors.get(0)), EntityModel.of(supervisors.get(1))));
        when(supervisorService.getAllSupervisors()).thenReturn(supervisors);
        when(assembler.toCollectionModel(supervisors)).thenReturn(collection);

        CollectionModel<EntityModel<Supervisor>> result = controller.getAllSupervisors();

        assertEquals(2, result.getContent().size());
        verify(supervisorService, times(1)).getAllSupervisors();
        verify(assembler, times(1)).toCollectionModel(supervisors);
    }

    @Test
    void createSupervisor_shouldReturnCreatedResponseWithModel() {
        when(supervisorService.saveSupervisor(any(Supervisor.class))).thenReturn(sampleSupervisor);
        when(assembler.toModel(sampleSupervisor)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<Supervisor>> response = controller.createSupervisor(sampleSupervisor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(supervisorService, times(1)).saveSupervisor(sampleSupervisor);
        verify(assembler, times(1)).toModel(sampleSupervisor);
    }

    @Test
    void validateSupervisor_withValidCode_shouldReturnAuthorizedStatus() {
        String code = "ABC123";
        when(supervisorService.findByCode(code)).thenReturn(sampleSupervisor);

        ResponseEntity<EntityModel<Boolean>> response = controller.validateSupervisor(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getContent());
        verify(supervisorService, times(1)).findByCode(code);
    }

    @Test
    void validateSupervisor_withInvalidCode_shouldReturnNotFound() {
        String code = "INVALID";
        when(supervisorService.findByCode(code)).thenReturn(null);

        ResponseEntity<EntityModel<Boolean>> response = controller.validateSupervisor(code);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(supervisorService, times(1)).findByCode(code);
    }

    @Test
    void deleteSupervisor_shouldReturnNoContent() {
        Integer id = 1;
        doNothing().when(supervisorService).deleteSupervisor(id);

        ResponseEntity<Void> response = controller.deleteSupervisor(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(supervisorService, times(1)).deleteSupervisor(id);
    }

    @Test
    void getSupervisorById_withValidId_shouldReturnOkResponseWithModel() {
        Integer id = 1;
        when(supervisorService.getSupervisorById(id)).thenReturn(sampleSupervisor);
        when(assembler.toModel(sampleSupervisor)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<Supervisor>> response = controller.getSupervisorById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(supervisorService, times(1)).getSupervisorById(id);
        verify(assembler, times(1)).toModel(sampleSupervisor);
    }

    @Test
    void getSupervisorById_withInvalidId_shouldReturnNotFound() {
        Integer id = 999;
        when(supervisorService.getSupervisorById(id)).thenReturn(null);

        ResponseEntity<EntityModel<Supervisor>> response = controller.getSupervisorById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(supervisorService, times(1)).getSupervisorById(id);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void updateSupervisor_shouldReturnOkResponseWithUpdatedModel() {
        Integer id = 1;
        Supervisor updated = new Supervisor();
        updated.setSupervisorName("Jane Doe");
        when(supervisorService.updateSupervisor(eq(id), any(Supervisor.class))).thenReturn(updated);
        when(assembler.toModel(updated)).thenReturn(EntityModel.of(updated));

        ResponseEntity<EntityModel<Supervisor>> response = controller.updateSupervisor(id, sampleSupervisor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EntityModel.of(updated), response.getBody());
        verify(supervisorService, times(1)).updateSupervisor(id, sampleSupervisor);
        verify(assembler, times(1)).toModel(updated);
    }

    @Test
    void updateSupervisor_whenServiceReturnsNull_shouldReturnNotFound() {
        Integer id = 999;
        when(supervisorService.updateSupervisor(eq(id), any(Supervisor.class))).thenReturn(null);

        ResponseEntity<EntityModel<Supervisor>> response = controller.updateSupervisor(id, sampleSupervisor);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(supervisorService, times(1)).updateSupervisor(id, sampleSupervisor);
        verify(assembler, never()).toModel(any());
    }
}