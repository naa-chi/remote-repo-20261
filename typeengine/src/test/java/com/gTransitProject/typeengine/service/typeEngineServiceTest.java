package com.gTransitProject.typeengine.service;

import com.gTransitProject.typeengine.model.typeEngine;
import com.gTransitProject.typeengine.repo.typeEngineRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class typeEngineServiceTest {

    @Mock
    private typeEngineRepo repo;          // mock the repository

    @InjectMocks
    private typeEngineService service;    // inject mocks into the service

    // ---------- getAll() ----------
    @Test
    void getAll_shouldReturnListFromRepo() {
        // given
        typeEngine engine1 = new typeEngine();
        engine1.setId(1);
        typeEngine engine2 = new typeEngine();
        engine2.setId(2);
        when(repo.findAll()).thenReturn(List.of(engine1, engine2));

        // when
        List<typeEngine> result = service.getAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(engine1, engine2);
        verify(repo, times(1)).findAll();
        verifyNoMoreInteractions(repo);
    }

    // ---------- getById() ----------
    @Test
    void getById_whenIdExists_shouldReturnTypeEngine() {
        // given
        typeEngine engine = new typeEngine();
        engine.setId(1);
        engine.setNameCodeEngine("ABC");
        when(repo.findById(1)).thenReturn(Optional.of(engine));

        // when
        typeEngine result = service.getById(1);

        // then
        assertThat(result).isSameAs(engine);
        verify(repo, times(1)).findById(1);
    }

    @Test
    void getById_whenIdDoesNotExist_shouldThrowRuntimeException() {
        // given
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> service.getById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No such id exists: 999");
        verify(repo, times(1)).findById(999);
    }

    // ---------- createTypeEngine() ----------
    @Test
    void createTypeEngine_shouldSaveAndReturnEntity() {
        // given
        typeEngine input = new typeEngine();
        input.setNameCodeEngine("XYZ");
        typeEngine saved = new typeEngine();
        saved.setId(100);
        saved.setNameCodeEngine("XYZ");
        when(repo.save(input)).thenReturn(saved);

        // when
        typeEngine result = service.createTypeEngine(input);

        // then
        assertThat(result).isSameAs(saved);
        verify(repo, times(1)).save(input);
    }

    // ---------- deleteTypeEngine() ----------
    @Test
    void deleteTypeEngine_shouldCallRepoDeleteById() {
        // when
        service.deleteTypeEngine(5);

        // then
        verify(repo, times(1)).deleteById(5);
        verifyNoMoreInteractions(repo);
    }

    // ---------- updateTypeEngine() ----------
    @Test
    void updateTypeEngine_whenIdExists_shouldUpdateAndReturn() {
        // given
        Integer id = 1;
        typeEngine existing = new typeEngine();
        existing.setId(id);
        existing.setNameCodeEngine("OLD");
        existing.setType("OLD_TYPE");

        typeEngine updatedData = new typeEngine();
        updatedData.setNameCodeEngine("NEW");
        updatedData.setType("NEW_TYPE");

        typeEngine savedEntity = new typeEngine();
        savedEntity.setId(id);
        savedEntity.setNameCodeEngine("NEW");
        savedEntity.setType("NEW_TYPE");

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(savedEntity);

        // when
        typeEngine result = service.updateTypeEngine(id, updatedData);

        // then
        assertThat(result).isSameAs(savedEntity);
        // verify that the existing entity was updated before saving
        assertThat(existing.getNameCodeEngine()).isEqualTo("NEW");
        assertThat(existing.getType()).isEqualTo("NEW_TYPE");
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(existing);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void updateTypeEngine_whenIdDoesNotExist_shouldThrowRuntimeException() {
        // given
        Integer id = 999;
        typeEngine updatedData = new typeEngine();
        when(repo.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> service.updateTypeEngine(id, updatedData))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No such id exists: " + id);
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }
}