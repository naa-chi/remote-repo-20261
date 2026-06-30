package com.gTransitProject.line.service;

import com.gTransitProject.line.model.Line;
import com.gTransitProject.line.repo.RepositoryLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceLineTest {

    @Mock
    private RepositoryLine lineRepo;

    @InjectMocks
    private ServiceLine serviceLine;

    @Test
    void getLines_shouldReturnListFromRepo() {
        Line l1 = new Line();
        Line l2 = new Line();
        when(lineRepo.findAll()).thenReturn(List.of(l1, l2));

        List<Line> result = serviceLine.getLines();

        assertThat(result).hasSize(2);
        verify(lineRepo, times(1)).findAll();
    }

    @Test
    void saveLine_shouldSaveAndReturn() {
        Line input = new Line();
        input.setLineNumber(5);
        Line saved = new Line();
        saved.setLineNumber(5);

        when(lineRepo.save(input)).thenReturn(saved);

        Line result = serviceLine.saveLine(input);

        assertThat(result).isSameAs(saved);
        verify(lineRepo, times(1)).save(input);
    }

    @Test
    void getLineById_whenExists_shouldReturnLine() {
        Line expected = new Line();
        when(lineRepo.findById(1)).thenReturn(Optional.of(expected));

        Line result = serviceLine.getLineById(1);

        assertThat(result).isSameAs(expected);
        verify(lineRepo, times(1)).findById(1);
    }

    @Test
    void getLineById_whenNotExists_shouldReturnNull() {
        when(lineRepo.findById(99)).thenReturn(Optional.empty());

        Line result = serviceLine.getLineById(99);

        assertThat(result).isNull();
        verify(lineRepo, times(1)).findById(99);
    }

    @Test
    void getLineByNumber_whenExists_shouldReturnLine() {
        Line expected = new Line();
        expected.setLineNumber(10);
        when(lineRepo.findByLineNumber(10)).thenReturn(Optional.of(expected));

        Line result = serviceLine.getLineByNumber(10);

        assertThat(result).isSameAs(expected);
        verify(lineRepo, times(1)).findByLineNumber(10);
    }

    @Test
    void getLineByNumber_whenNotExists_shouldReturnNull() {
        when(lineRepo.findByLineNumber(999)).thenReturn(Optional.empty());

        Line result = serviceLine.getLineByNumber(999);

        assertThat(result).isNull();
        verify(lineRepo, times(1)).findByLineNumber(999);
    }

    @Test
    void deleteLine_shouldCallRepoDeleteById() {
        serviceLine.deleteLine(5);
        verify(lineRepo, times(1)).deleteById(5);
        verifyNoMoreInteractions(lineRepo);
    }

    @Test
    void updateLine_whenExists_shouldUpdateFieldsAndSave() {
        Integer id = 1;

        Line existing = new Line();
        existing.setLineNumber(5);
        existing.setLengthInKm(100);   // Changed from 100.0 to 100

        Line updatedData = new Line();
        updatedData.setLineNumber(10);
        updatedData.setLengthInKm(200); // Changed from 200.0 to 200

        when(lineRepo.findById(id)).thenReturn(Optional.of(existing));
        when(lineRepo.save(existing)).thenReturn(existing);

        Line result = serviceLine.updateLine(id, updatedData);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getLineNumber()).isEqualTo(10);
        assertThat(existing.getLengthInKm()).isEqualTo(200); // Changed from 200.0 to 200

        verify(lineRepo, times(1)).findById(id);
        verify(lineRepo, times(1)).save(existing);
    }

    @Test
    void updateLine_whenNotExists_shouldReturnNull() {
        Integer id = 99;
        when(lineRepo.findById(id)).thenReturn(Optional.empty());

        Line result = serviceLine.updateLine(id, new Line());

        assertThat(result).isNull();
        verify(lineRepo, times(1)).findById(id);
        verify(lineRepo, never()).save(any());
    }
}