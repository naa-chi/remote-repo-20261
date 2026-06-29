package com.gTransitProject.typeengine.controller;

import com.gTransitProject.typeengine.assembler.engineModelAssembler;   // new assembler
import com.gTransitProject.typeengine.model.typeEngine;
import com.gTransitProject.typeengine.service.typeEngineService;
import com.gTransitProject.typeengine.typeEngineDTO.engineDTO;          // DTO
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class typeEngineControllerTests {

    @Mock
    private typeEngineService service;

    @Mock
    private engineModelAssembler assembler;   // new assembler

    @InjectMocks
    private typeEngineController controller;

    @BeforeEach
    void setUp() {
        // Stub the assembler to return an EntityModel<engineDTO>
        // We use lenient() because the delete test doesn't use the assembler.
        lenient().when(assembler.toModel(any(typeEngine.class))).thenAnswer(invocation -> {
            typeEngine entity = invocation.getArgument(0);
            engineDTO dto = new engineDTO();
            dto.setId(entity.getId());
            dto.setTypeCodeEngine(entity.getTypeCodeEngine());
            dto.setHorsepower(entity.getHorsepower());
            return EntityModel.of(dto);
        });
    }

    @Test
    public void testCreateTypeEngine() {
        typeEngine input = new typeEngine();
        input.setType("V8");
        input.setHorsepower(450.0f);
        input.setTypeCodeEngine(100);
        input.setNameCodeEngine("V8-Engine");

        typeEngine saved = new typeEngine();
        saved.setId(1);
        saved.setType("V8");
        saved.setHorsepower(450.0f);
        saved.setTypeCodeEngine(100);
        saved.setNameCodeEngine("V8-Engine");

        when(service.createTypeEngine(any(typeEngine.class))).thenReturn(saved);

        ResponseEntity<EntityModel<engineDTO>> response = controller.createTypeEngine(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());

        engineDTO result = response.getBody().getContent();
        assertEquals(1, result.getId());
        assertEquals(100, result.getTypeCodeEngine());
        assertEquals(450.0f, result.getHorsepower());
    }

    @Test
    public void testGetAll() {
        typeEngine engine1 = new typeEngine(1, "V8", 450.0f, 100, "V8-Engine");
        typeEngine engine2 = new typeEngine(2, "Electric", 0f, 200, "Elec-Engine");

        List<typeEngine> engines = Arrays.asList(engine1, engine2);
        when(service.getAll()).thenReturn(engines);

        ResponseEntity<CollectionModel<EntityModel<engineDTO>>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        CollectionModel<EntityModel<engineDTO>> body = response.getBody();
        assertNotNull(body);

        List<engineDTO> dtos = body.getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());

        assertEquals(2, dtos.size());
        assertEquals(100, dtos.get(0).getTypeCodeEngine());
        assertEquals(450.0f, dtos.get(0).getHorsepower());
        assertEquals(200, dtos.get(1).getTypeCodeEngine());
        assertEquals(0f, dtos.get(1).getHorsepower());

        assertTrue(body.getLinks().hasLink("self"));
    }

    @Test
    public void testGetById() {
        typeEngine engine = new typeEngine(1, "V8", 450.0f, 100, "V8-Engine");
        when(service.getById(1)).thenReturn(engine);

        ResponseEntity<EntityModel<engineDTO>> response = controller.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        engineDTO result = response.getBody().getContent();
        assertEquals(1, result.getId());
        assertEquals(100, result.getTypeCodeEngine());
        assertEquals(450.0f, result.getHorsepower());
    }

    @Test
    public void testUpdateTypeEngine() {
        typeEngine updated = new typeEngine(1, "V12", 600.0f, 101, "V12-Engine");
        when(service.updateTypeEngine(eq(1), any(typeEngine.class))).thenReturn(updated);

        ResponseEntity<EntityModel<engineDTO>> response = controller.updateTypeEngine(1, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        engineDTO result = response.getBody().getContent();
        assertEquals(1, result.getId());
        assertEquals(101, result.getTypeCodeEngine());
        assertEquals(600.0f, result.getHorsepower());
    }

    @Test
    public void testDeleteTypeEngine() {
        doNothing().when(service).deleteTypeEngine(1);

        ResponseEntity<Void> response = controller.deleteTypeEngine(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteTypeEngine(1);
    }
}