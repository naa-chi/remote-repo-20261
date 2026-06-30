package com.gTransitProject.driver.service;

import com.gTransitProject.driver.exception.resourceNotFoundException;
import com.gTransitProject.driver.model.driverModel;
import com.gTransitProject.driver.repository.driverRepository;
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
class driverServiceTest {

    @Mock
    private driverRepository driverRepository;

    @InjectMocks
    private driverService service;

    @Test
    void createDriver_shouldSaveAndReturn() {
        driverModel input = new driverModel();
        input.setDriverName("John Doe");
        // No need to set ID
        driverModel saved = new driverModel();
        saved.setDriverName("John Doe");

        when(driverRepository.save(input)).thenReturn(saved);

        driverModel result = service.createDriver(input);

        assertThat(result).isSameAs(saved);
        verify(driverRepository, times(1)).save(input);
    }

    @Test
    void getAllDrivers_shouldReturnListFromRepo() {
        driverModel d1 = new driverModel();
        driverModel d2 = new driverModel();
        when(driverRepository.findAll()).thenReturn(List.of(d1, d2));

        List<driverModel> result = service.getAllDrivers();

        assertThat(result).hasSize(2);
        verify(driverRepository, times(1)).findAll();
    }

    @Test
    void getDriverById_whenExists_shouldReturnDriver() {
        driverModel expected = new driverModel();
        // No ID set, but that's fine – the service doesn't inspect it.
        when(driverRepository.findById(1L)).thenReturn(Optional.of(expected));

        driverModel result = service.getDriverById(1L);

        assertThat(result).isSameAs(expected);
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void getDriverById_whenNotExists_shouldThrowResourceNotFoundException() {
        when(driverRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDriverById(99L))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Driver not found with id: 99");
        verify(driverRepository, times(1)).findById(99L);
    }

    @Test
    void updateDriver_whenExists_shouldUpdateFieldsAndSave() {
        Long id = 1L;

        driverModel existing = new driverModel();
        existing.setDriverName("Old Name");
        existing.setDriverLicenseNumber("OLD123");

        driverModel updatedData = new driverModel();
        updatedData.setDriverName("New Name");
        updatedData.setDriverLicenseNumber("NEW456");

        when(driverRepository.findById(id)).thenReturn(Optional.of(existing));
        when(driverRepository.save(existing)).thenReturn(existing);

        driverModel result = service.updateDriver(id, updatedData);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getDriverName()).isEqualTo("New Name");
        assertThat(existing.getDriverLicenseNumber()).isEqualTo("NEW456");

        verify(driverRepository, times(1)).findById(id);
        verify(driverRepository, times(1)).save(existing);
    }

    @Test
    void updateDriver_whenNotExists_shouldThrowResourceNotFoundException() {
        Long id = 99L;
        when(driverRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateDriver(id, new driverModel()))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Driver not found with id: " + id);
        verify(driverRepository, times(1)).findById(id);
        verify(driverRepository, never()).save(any());
    }

    @Test
    void deleteDriver_whenExists_shouldDelete() {
        Long id = 1L;
        driverModel existing = new driverModel();
        when(driverRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(driverRepository).delete(existing);

        service.deleteDriver(id);

        verify(driverRepository, times(1)).findById(id);
        verify(driverRepository, times(1)).delete(existing);
    }

    @Test
    void deleteDriver_whenNotExists_shouldThrowResourceNotFoundException() {
        Long id = 99L;
        when(driverRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteDriver(id))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Driver not found with id: " + id);
        verify(driverRepository, times(1)).findById(id);
        verify(driverRepository, never()).delete(any());
    }
}