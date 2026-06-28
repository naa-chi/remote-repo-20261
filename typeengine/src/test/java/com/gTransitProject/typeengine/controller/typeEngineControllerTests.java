package com.gTransitProject.typeengine.controller;

import com.gTransitProject.typeengine.model.typeEngine;
import com.gTransitProject.typeengine.service.typeEngineService;
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
class typeengineControllerTest {

    @Mock
    private typeEngineService service;

    @InjectMocks
    private typeEngineController controller;

    private typeEngine sampleTypeEngine;

    @BeforeEach
    void setUp() {
        sampleTypeEngine = new typeEngine();
        sampleTypeEngine.setId(1);
        sampleTypeEngine.setType("Diesel");
        sampleTypeEngine.setHorsepower(3500.5f);
        sampleTypeEngine.setTypeCodeEngine(101);
        sampleTypeEngine.setNameCodeEngine("DIESEL_ENGINE_101");
    }

    @Test
    void createTypeEngine_shouldReturnCreatedTypeEngineWithStatus201() {
        when(service.createTypeEngine(any(typeEngine.class))).thenReturn(sampleTypeEngine);

        ResponseEntity<typeEngine> response = controller.createTypeEngine(sampleTypeEngine);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleTypeEngine, response.getBody());
        verify(service, times(1)).createTypeEngine(sampleTypeEngine);
    }

    @Test
    void getAll_shouldReturnListOfTypeEngines() {
        List<typeEngine> engines = Arrays.asList(sampleTypeEngine, new typeEngine());
        when(service.getAll()).thenReturn(engines);

        List<typeEngine> result = controller.getAll();

        assertEquals(2, result.size());
        verify(service, times(1)).getAll();
    }

    @Test
    void getById_withValidId_shouldReturnTypeEngine() {
        Integer id = 1;
        when(service.getById(id)).thenReturn(sampleTypeEngine);

        ResponseEntity<typeEngine> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleTypeEngine, response.getBody());
        verify(service, times(1)).getById(id);
    }

    @Test
    void getById_withInvalidId_shouldReturnNull() {
        Integer id = 999;
        when(service.getById(id)).thenReturn(null);

        ResponseEntity<typeEngine> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).getById(id);
    }

    @Test
    void updateTypeEngine_shouldReturnUpdatedTypeEngine() {
        Integer id = 1;
        typeEngine updated = new typeEngine();
        updated.setId(1);
        updated.setType("Electric");
        when(service.updateTypeEngine(eq(id), any(typeEngine.class))).thenReturn(updated);

        ResponseEntity<typeEngine> response = controller.updateTypeEngine(id, sampleTypeEngine);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(service, times(1)).updateTypeEngine(id, sampleTypeEngine);
    }

    @Test
    void deleteTypeEngine_shouldReturnNoContent() {
        Integer id = 1;
        doNothing().when(service).deleteTypeEngine(id);

        ResponseEntity<Void> response = controller.deleteTypeEngine(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).deleteTypeEngine(id);
    }
}