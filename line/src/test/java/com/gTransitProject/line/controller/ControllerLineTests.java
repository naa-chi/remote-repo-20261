package com.gTransitProject.line.controller;

import com.gTransitProject.line.model.Line;
import com.gTransitProject.line.service.ServiceLine;
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
class ControllerLineTest {

    @Mock
    private ServiceLine lineServ;

    @Mock
    private LineAssembler assembler;

    @InjectMocks
    private ControllerLine controller;

    private Line sampleLine;
    private EntityModel<Line> sampleModel;

    @BeforeEach
    void setUp() {
        sampleLine = new Line();
        sampleLine.setId(1);
        sampleLine.setLineNumber(101);
        sampleLine.setLengthInKm(250);

        sampleModel = EntityModel.of(sampleLine);
    }

    @Test
    void getLines_shouldReturnCollectionModelWithSelfLink() {
        List<Line> lines = Arrays.asList(sampleLine, new Line());
        CollectionModel<EntityModel<Line>> collection = CollectionModel.of(
                Arrays.asList(EntityModel.of(lines.get(0)), EntityModel.of(lines.get(1))));
        when(lineServ.getLines()).thenReturn(lines);
        when(assembler.toCollectionModel(lines)).thenReturn(collection);

        CollectionModel<EntityModel<Line>> result = controller.getLines();

        assertEquals(2, result.getContent().size());
        verify(lineServ, times(1)).getLines();
        verify(assembler, times(1)).toCollectionModel(lines);
    }

    @Test
    void getLineById_withValidId_shouldReturnOkResponseWithModel() {
        Integer id = 1;
        when(lineServ.getLineById(id)).thenReturn(sampleLine);
        when(assembler.toModel(sampleLine)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<Line>> response = controller.getLineById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(lineServ, times(1)).getLineById(id);
        verify(assembler, times(1)).toModel(sampleLine);
    }

    @Test
    void getLineById_withInvalidId_shouldReturnNotFound() {
        Integer id = 999;
        when(lineServ.getLineById(id)).thenReturn(null);

        ResponseEntity<EntityModel<Line>> response = controller.getLineById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(lineServ, times(1)).getLineById(id);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void getLineByNumber_withValidNumber_shouldReturnOkResponseWithModel() {
        Integer lineNumber = 101;
        when(lineServ.getLineByNumber(lineNumber)).thenReturn(sampleLine);
        when(assembler.toModel(sampleLine)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<Line>> response = controller.getLineByNumber(lineNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(lineServ, times(1)).getLineByNumber(lineNumber);
        verify(assembler, times(1)).toModel(sampleLine);
    }

    @Test
    void getLineByNumber_withInvalidNumber_shouldReturnNotFound() {
        Integer lineNumber = 999;
        when(lineServ.getLineByNumber(lineNumber)).thenReturn(null);

        ResponseEntity<EntityModel<Line>> response = controller.getLineByNumber(lineNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(lineServ, times(1)).getLineByNumber(lineNumber);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void saveLine_shouldReturnCreatedResponseWithModel() {
        when(lineServ.saveLine(any(Line.class))).thenReturn(sampleLine);
        when(assembler.toModel(sampleLine)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<Line>> response = controller.saveLine(sampleLine);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(lineServ, times(1)).saveLine(sampleLine);
        verify(assembler, times(1)).toModel(sampleLine);
    }

    @Test
    void updateLine_shouldReturnOkResponseWithUpdatedModel() {
        Integer id = 1;
        Line updated = new Line();
        updated.setId(1);
        updated.setLineNumber(102);
        when(lineServ.updateLine(eq(id), any(Line.class))).thenReturn(updated);
        when(assembler.toModel(updated)).thenReturn(EntityModel.of(updated));

        ResponseEntity<EntityModel<Line>> response = controller.updateLine(id, sampleLine);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EntityModel.of(updated), response.getBody());
        verify(lineServ, times(1)).updateLine(id, sampleLine);
        verify(assembler, times(1)).toModel(updated);
    }

    @Test
    void updateLine_withInvalidId_shouldReturnNotFound() {
        Integer id = 999;
        when(lineServ.updateLine(eq(id), any(Line.class))).thenReturn(null);

        ResponseEntity<EntityModel<Line>> response = controller.updateLine(id, sampleLine);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(lineServ, times(1)).updateLine(id, sampleLine);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void deleteLine_shouldReturnNoContent() {
        Integer id = 1;
        doNothing().when(lineServ).deleteLine(id);

        ResponseEntity<Void> response = controller.deleteLine(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(lineServ, times(1)).deleteLine(id);
    }
}