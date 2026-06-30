package com.gTransitProject.supervisor.service;

import com.gTransitProject.supervisor.exception.ResourceNotFoundException;
import com.gTransitProject.supervisor.model.Supervisor;
import com.gTransitProject.supervisor.repo.SupervisorRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupervisorServiceTest {

    @Mock
    private SupervisorRepository supervisorRepository;

    @InjectMocks
    private SupervisorService supervisorService;

    @Test
    void getAllSupervisors_shouldReturnListFromRepo() {
        Supervisor s1 = new Supervisor();
        Supervisor s2 = new Supervisor();
        when(supervisorRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Supervisor> result = supervisorService.getAllSupervisors();

        assertThat(result).hasSize(2);
        verify(supervisorRepository, times(1)).findAll();
    }

    @Test
    void saveSupervisor_shouldSaveAndReturn() {
        Supervisor input = new Supervisor();
        input.setSupervisorCode("SUP001");
        Supervisor saved = new Supervisor();
        saved.setSupervisorCode("SUP001");
        // Removed saved.setId(1);

        when(supervisorRepository.save(input)).thenReturn(saved);

        Supervisor result = supervisorService.saveSupervisor(input);

        assertThat(result).isSameAs(saved);
        verify(supervisorRepository, times(1)).save(input);
    }

    @Test
    void getSupervisorById_whenExists_shouldReturnSupervisor() {
        Supervisor expected = new Supervisor();
        // Removed expected.setId(1);
        when(supervisorRepository.findById(1)).thenReturn(Optional.of(expected));

        Supervisor result = supervisorService.getSupervisorById(1);

        assertThat(result).isSameAs(expected);
        verify(supervisorRepository, times(1)).findById(1);
    }

    @Test
    void getSupervisorById_whenNotExists_shouldThrowResourceNotFoundException() {
        when(supervisorRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> supervisorService.getSupervisorById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Supervisor no encontrado");
        verify(supervisorRepository, times(1)).findById(99);
    }

    @Test
    void findByCode_whenExists_shouldReturnSupervisor() {
        Supervisor expected = new Supervisor();
        expected.setSupervisorCode("SUP123");
        when(supervisorRepository.findBySupervisorCode("SUP123"))
                .thenReturn(Optional.of(expected));

        Supervisor result = supervisorService.findByCode("SUP123");

        assertThat(result).isSameAs(expected);
        verify(supervisorRepository, times(1)).findBySupervisorCode("SUP123");
    }

    @Test
    void findByCode_whenNotExists_shouldThrowResourceNotFoundException() {
        when(supervisorRepository.findBySupervisorCode("UNKNOWN"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> supervisorService.findByCode("UNKNOWN"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Supervisor no encontrado");
        verify(supervisorRepository, times(1)).findBySupervisorCode("UNKNOWN");
    }

    @Test
    void deleteSupervisor_shouldCallRepoDeleteById() {
        supervisorService.deleteSupervisor(5);
        verify(supervisorRepository, times(1)).deleteById(5);
        verifyNoMoreInteractions(supervisorRepository);
    }

    @Test
    void updateSupervisor_whenExists_shouldUpdateFieldsAndSave() {
        Integer id = 1;

        Supervisor existing = new Supervisor();
        // removed existing.setId(id);
        existing.setSupervisorName("Old Name");
        existing.setSupervisorCode("OLD001");
        existing.setCityCode("OLD_CITY");
        existing.setAuthorized(false);

        Supervisor updatedData = new Supervisor();
        updatedData.setSupervisorName("New Name");
        updatedData.setSupervisorCode("NEW001");
        updatedData.setCityCode("NEW_CITY");
        updatedData.setAuthorized(true);

        when(supervisorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(supervisorRepository.save(existing)).thenReturn(existing);

        Supervisor result = supervisorService.updateSupervisor(id, updatedData);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getSupervisorName()).isEqualTo("New Name");
        assertThat(existing.getSupervisorCode()).isEqualTo("NEW001");
        assertThat(existing.getCityCode()).isEqualTo("NEW_CITY");
        assertThat(existing.getAuthorized()).isTrue();

        verify(supervisorRepository, times(1)).findById(id);
        verify(supervisorRepository, times(1)).save(existing);
    }

    @Test
    void updateSupervisor_whenNotExists_shouldThrowResourceNotFoundException() {
        Integer id = 99;
        Supervisor updatedData = new Supervisor();

        when(supervisorRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> supervisorService.updateSupervisor(id, updatedData))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Supervisor no encontrado");

        verify(supervisorRepository, times(1)).findById(id);
        verify(supervisorRepository, never()).save(any());
    }
}