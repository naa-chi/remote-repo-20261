package com.gTransitProject.station.service;

import com.gTransitProject.station.model.station;
import com.gTransitProject.station.repo.RepositoryStation;
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
class ServiceStationTest {

    @Mock
    private RepositoryStation stationRepo;

    @InjectMocks
    private ServiceStation serviceStation;

    @Test
    void getStations_shouldReturnListFromRepo() {
        station s1 = new station();
        station s2 = new station();
        when(stationRepo.findAll()).thenReturn(List.of(s1, s2));

        List<station> result = serviceStation.getStations();

        assertThat(result).hasSize(2);
        verify(stationRepo, times(1)).findAll();
    }

    @Test
    void saveStation_shouldSaveAndReturn() {
        station input = new station();
        input.setStationName("Central");
        station saved = new station();
        saved.setStationName("Central");

        when(stationRepo.save(input)).thenReturn(saved);

        station result = serviceStation.saveStation(input);

        assertThat(result).isSameAs(saved);
        verify(stationRepo, times(1)).save(input);
    }

    @Test
    void getStationById_whenExists_shouldReturnStation() {
        station expected = new station();
        when(stationRepo.findById(1)).thenReturn(Optional.of(expected));

        station result = serviceStation.getStationById(1);

        assertThat(result).isSameAs(expected);
        verify(stationRepo, times(1)).findById(1);
    }

    @Test
    void getStationById_whenNotExists_shouldReturnNull() {
        when(stationRepo.findById(99)).thenReturn(Optional.empty());

        station result = serviceStation.getStationById(99);

        assertThat(result).isNull();
        verify(stationRepo, times(1)).findById(99);
    }

    @Test
    void deleteStation_shouldCallRepoDeleteById() {
        serviceStation.deleteStation(5);
        verify(stationRepo, times(1)).deleteById(5);
        verifyNoMoreInteractions(stationRepo);
    }

    @Test
    void updateStation_whenExists_shouldUpdateFieldsAndSave() {
        Integer id = 1;

        station existing = new station();
        existing.setUniqueStationCode("OLD_CODE");
        existing.setStationName("Old Name");
        existing.setCityCode("OLD_CITY");
        existing.setLineNumber(1);

        station updatedData = new station();
        updatedData.setUniqueStationCode("NEW_CODE");
        updatedData.setStationName("New Name");
        updatedData.setCityCode("NEW_CITY");
        updatedData.setLineNumber(2);

        when(stationRepo.findById(id)).thenReturn(Optional.of(existing));
        when(stationRepo.save(existing)).thenReturn(existing);

        station result = serviceStation.updateStation(id, updatedData);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getUniqueStationCode()).isEqualTo("NEW_CODE");
        assertThat(existing.getStationName()).isEqualTo("New Name");
        assertThat(existing.getCityCode()).isEqualTo("NEW_CITY");
        assertThat(existing.getLineNumber()).isEqualTo(2);

        verify(stationRepo, times(1)).findById(id);
        verify(stationRepo, times(1)).save(existing);
    }

    @Test
    void updateStation_whenNotExists_shouldReturnNull() {
        Integer id = 99;
        station updatedData = new station();

        when(stationRepo.findById(id)).thenReturn(Optional.empty());

        station result = serviceStation.updateStation(id, updatedData);

        assertThat(result).isNull();
        verify(stationRepo, times(1)).findById(id);
        verify(stationRepo, never()).save(any());
    }
}