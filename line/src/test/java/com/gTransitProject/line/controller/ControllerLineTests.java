package com.gTransitProject.line.controller;

import com.gTransitProject.line.model.Line;
import com.gTransitProject.line.service.ServiceLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerLineTest {

    @Mock
    private ServiceLine lineServ;

    @InjectMocks
    private ControllerLine controller;

    private Line sampleLine;

    @BeforeEach
    void setUp() {
        sampleLine = new Line();
        sampleLine.setId(1);
        sampleLine.setLineNumber(101);
        sampleLine.setLengthInKm(250);
    }

    @Test
    void getLines_shouldReturnListOfLines() {
        List<Line> lines = Arrays.asList(sampleLine, new Line());
        when(lineServ.getLines()).thenReturn(lines);

        List<Line> result = controller.getLines();

        assertEquals(2, result.size());
        verify(lineServ, times(1)).getLines();
    }

    @Test
    void getLineById_withValidId_shouldReturnLine() {
        Integer id = 1;
        when(lineServ.getLineById(id)).thenReturn(sampleLine);

        Line result = controller.getLineById(id);

        assertEquals(sampleLine, result);
        verify(lineServ, times(1)).getLineById(id);
    }

    @Test
    void getLineById_withInvalidId_shouldReturnNull() {
        Integer id = 999;
        when(lineServ.getLineById(id)).thenReturn(null);

        Line result = controller.getLineById(id);

        assertNull(result);
        verify(lineServ, times(1)).getLineById(id);
    }

    @Test
    void getLineByNumber_withValidNumber_shouldReturnLine() {
        Integer lineNumber = 101;
        when(lineServ.getLineByNumber(lineNumber)).thenReturn(sampleLine);

        Line result = controller.getLineByNumber(lineNumber);

        assertEquals(sampleLine, result);
        verify(lineServ, times(1)).getLineByNumber(lineNumber);
    }

    @Test
    void getLineByNumber_withInvalidNumber_shouldReturnNull() {
        Integer lineNumber = 999;
        when(lineServ.getLineByNumber(lineNumber)).thenReturn(null);

        Line result = controller.getLineByNumber(lineNumber);

        assertNull(result);
        verify(lineServ, times(1)).getLineByNumber(lineNumber);
    }

    @Test
    void saveLine_shouldReturnSavedLine() {
        when(lineServ.saveLine(any(Line.class))).thenReturn(sampleLine);

        Line result = controller.saveLine(sampleLine);

        assertEquals(sampleLine, result);
        verify(lineServ, times(1)).saveLine(sampleLine);
    }

    @Test
    void updateLine_shouldReturnUpdatedLine() {
        Integer id = 1;
        Line updated = new Line();
        updated.setId(1);
        updated.setLineNumber(102);
        when(lineServ.updateLine(eq(id), any(Line.class))).thenReturn(updated);

        Line result = controller.updateLine(id, sampleLine);

        assertEquals(updated, result);
        verify(lineServ, times(1)).updateLine(id, sampleLine);
    }

    @Test
    void deleteLine_shouldCallServiceDelete() {
        Integer id = 1;
        doNothing().when(lineServ).deleteLine(id);

        controller.deleteLine(id);

        verify(lineServ, times(1)).deleteLine(id);
    }
}