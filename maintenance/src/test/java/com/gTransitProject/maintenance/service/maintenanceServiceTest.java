package com.gTransitProject.maintenance.service;

import com.gTransitProject.maintenance.exception.resourceNotFoundException;
import com.gTransitProject.maintenance.model.maintenanceModel;
import com.gTransitProject.maintenance.repo.maintenanceRepo;
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
class maintenanceServiceTest {

    @Mock
    private maintenanceRepo maintenanceRepo;

    @InjectMocks
    private maintenanceService service;

    // ---------- createMaintenance ----------
    @Test
    void createMaintenance_shouldSaveAndReturn() {
        maintenanceModel input = new maintenanceModel();
        input.setVehicleId(100);
        maintenanceModel saved = new maintenanceModel();
        saved.setId(1);
        saved.setVehicleId(100);

        when(maintenanceRepo.save(input)).thenReturn(saved);

        maintenanceModel result = service.createMaintenance(input);

        assertThat(result).isSameAs(saved);
        verify(maintenanceRepo, times(1)).save(input);
    }

    // ---------- getAllMaintenance ----------
    @Test
    void getAllMaintenance_shouldReturnListFromRepo() {
        maintenanceModel m1 = new maintenanceModel();
        maintenanceModel m2 = new maintenanceModel();
        when(maintenanceRepo.findAll()).thenReturn(List.of(m1, m2));

        List<maintenanceModel> result = service.getAllMaintenance();

        assertThat(result).hasSize(2);
        verify(maintenanceRepo, times(1)).findAll();
    }

    // ---------- getMaintenanceById ----------
    @Test
    void getMaintenanceById_whenExists_shouldReturnRecord() {
        maintenanceModel expected = new maintenanceModel();
        expected.setId(1);
        when(maintenanceRepo.findById(1)).thenReturn(Optional.of(expected));

        maintenanceModel result = service.getMaintenanceById(1);

        assertThat(result).isSameAs(expected);
        verify(maintenanceRepo, times(1)).findById(1);
    }

    @Test
    void getMaintenanceById_whenNotExists_shouldThrowResourceNotFoundException() {
        when(maintenanceRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getMaintenanceById(99))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Maintenance record not found with id: 99");
        verify(maintenanceRepo, times(1)).findById(99);
    }

    // ---------- getMaintenanceByVehicleId (uses findAll stream) ----------
    @Test
    void getMaintenanceByVehicleId_whenExists_shouldReturnRecord() {
        maintenanceModel m1 = new maintenanceModel();
        m1.setVehicleId(10);
        maintenanceModel m2 = new maintenanceModel();
        m2.setVehicleId(20);
        when(maintenanceRepo.findAll()).thenReturn(List.of(m1, m2));

        maintenanceModel result = service.getMaintenanceByVehicleId(10);

        assertThat(result).isSameAs(m1);
        verify(maintenanceRepo, times(1)).findAll();
    }

    @Test
    void getMaintenanceByVehicleId_whenNotExists_shouldThrowResourceNotFoundException() {
        when(maintenanceRepo.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> service.getMaintenanceByVehicleId(999))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Maintenance record not found with vehicle ID: 999");
        verify(maintenanceRepo, times(1)).findAll();
    }

    // ---------- updateMaintenance ----------
    @Test
    void updateMaintenance_whenExists_shouldSetIdAndSave() {
        Integer id = 1;

        // We need to mock that the record exists for getMaintenanceById.
        // The service calls getMaintenanceById(id) which internally calls findById.
        // So we mock that findById returns an existing record.
        maintenanceModel existing = new maintenanceModel();
        existing.setId(id);
        when(maintenanceRepo.findById(id)).thenReturn(Optional.of(existing));

        // The updated data passed in (without ID)
        maintenanceModel updatedData = new maintenanceModel();
        updatedData.setVehicleId(999);
        // We'll save it after setId() is called.
        // We want to verify that setId(id) was called and that save is called with that object.
        // We'll use a capture or simply verify that save is called with any maintenanceModel.
        // Since we can't easily capture the modified object with a simple mock,
        // we can use doAnswer or verify that the object passed to save has the ID set.
        // We'll use a spy or argument captor – simplest is to mock save to return the saved object
        // and then assert that the argument passed had the ID set.

        // Mock save to return the same object (we'll capture via ArgumentCaptor)
        when(maintenanceRepo.save(any(maintenanceModel.class))).thenAnswer(invocation -> {
            maintenanceModel arg = invocation.getArgument(0);
            // The service should have set the ID before calling save
            assertThat(arg.getId()).isEqualTo(id);
            return arg;
        });

        maintenanceModel result = service.updateMaintenance(id, updatedData);

        // Verify findById was called (for existence check)
        verify(maintenanceRepo, times(1)).findById(id);
        // Verify save was called exactly once
        verify(maintenanceRepo, times(1)).save(any(maintenanceModel.class));
        // We can also verify that the updatedData's fields were not changed (but the ID was set on it)
        // Actually the service sets the ID on the updatedData object, so we can check that.
        assertThat(updatedData.getId()).isEqualTo(id);
        // The result should be the saved object
        assertThat(result).isSameAs(updatedData);
        // Optionally verify the fields were carried over
        assertThat(result.getVehicleId()).isEqualTo(999);
    }

    // Alternative simpler test: we can just verify that the save was called and that the updatedData ID was set.
    // But the above is fine.

    @Test
    void updateMaintenance_whenNotExists_shouldThrowResourceNotFoundException() {
        Integer id = 99;
        when(maintenanceRepo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateMaintenance(id, new maintenanceModel()))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Maintenance record not found with id: " + id);
        verify(maintenanceRepo, times(1)).findById(id);
        verify(maintenanceRepo, never()).save(any());
    }
}