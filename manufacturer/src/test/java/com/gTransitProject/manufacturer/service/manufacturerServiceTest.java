package com.gTransitProject.manufacturer.service;

import com.gTransitProject.manufacturer.model.manufacturerModel;
import com.gTransitProject.manufacturer.repo.manufacturerRepo;
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
class manufacturerServiceTest {

    @Mock
    private manufacturerRepo repo;

    @InjectMocks
    private manufacturerService service;

    // ---------- getAll ----------
    @Test
    void getAll_shouldReturnListFromRepo() {
        manufacturerModel m1 = new manufacturerModel();
        manufacturerModel m2 = new manufacturerModel();
        when(repo.findAll()).thenReturn(List.of(m1, m2));

        List<manufacturerModel> result = service.getAll();

        assertThat(result).hasSize(2);
        verify(repo, times(1)).findAll();
    }

    // ---------- getById ----------
    @Test
    void getById_whenExists_shouldReturnManufacturer() {
        manufacturerModel expected = new manufacturerModel();
        expected.setId(1);
        when(repo.findById(1)).thenReturn(Optional.of(expected));

        manufacturerModel result = service.getById(1);

        assertThat(result).isSameAs(expected);
        verify(repo, times(1)).findById(1);
    }

    @Test
    void getById_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Manufacturer not found with id: 99");
        verify(repo, times(1)).findById(99);
    }

    // ---------- getByName (custom repo method) ----------
    @Test
    void getByName_whenExists_shouldReturnManufacturer() {
        manufacturerModel expected = new manufacturerModel();
        expected.setName("Siemens");
        when(repo.findByName("Siemens")).thenReturn(Optional.of(expected));

        manufacturerModel result = service.getByName("Siemens");

        assertThat(result).isSameAs(expected);
        verify(repo, times(1)).findByName("Siemens");
    }

    @Test
    void getByName_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findByName("Unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByName("Unknown"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Manufacturer not found with name: Unknown");
        verify(repo, times(1)).findByName("Unknown");
    }

    // ---------- getByCountry (custom repo method, returns List) ----------
    @Test
    void getByCountry_shouldReturnListFromRepo() {
        manufacturerModel m1 = new manufacturerModel();
        manufacturerModel m2 = new manufacturerModel();
        when(repo.findByCountry("Germany")).thenReturn(List.of(m1, m2));

        List<manufacturerModel> result = service.getByCountry("Germany");

        assertThat(result).hasSize(2);
        verify(repo, times(1)).findByCountry("Germany");
    }

    // ---------- create ----------
    @Test
    void create_shouldSaveAndReturn() {
        manufacturerModel input = new manufacturerModel();
        input.setName("Bombardier");
        manufacturerModel saved = new manufacturerModel();
        saved.setId(1);
        saved.setName("Bombardier");

        when(repo.save(input)).thenReturn(saved);

        manufacturerModel result = service.create(input);

        assertThat(result).isSameAs(saved);
        verify(repo, times(1)).save(input);
    }

    // ---------- delete ----------
    @Test
    void delete_shouldCallRepoDeleteById() {
        service.delete(5);
        verify(repo, times(1)).deleteById(5);
        verifyNoMoreInteractions(repo);
    }

    // ---------- updateManufacturer ----------
    @Test
    void updateManufacturer_whenExists_shouldUpdateFieldsAndSave() {
        Integer id = 1;
        manufacturerModel existing = new manufacturerModel();
        existing.setName("Old Name");
        existing.setCountry("Old Country");

        manufacturerModel updatedData = new manufacturerModel();
        updatedData.setName("New Name");
        updatedData.setCountry("New Country");

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        manufacturerModel result = service.updateManufacturer(id, updatedData);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getName()).isEqualTo("New Name");
        assertThat(existing.getCountry()).isEqualTo("New Country");

        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(existing);
    }

    @Test
    void updateManufacturer_whenNotExists_shouldThrowRuntimeException() {
        Integer id = 99;
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateManufacturer(id, new manufacturerModel()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Manufacturer not found with id: " + id);
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }
}