package com.gTransitProject.city.service;

import com.gTransitProject.city.exception.businessException;
import com.gTransitProject.city.exception.resourceNotFoundException;
import com.gTransitProject.city.model.City;
import com.gTransitProject.city.repo.CityRepository;
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
class CityServiceTests {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    // ---------- getAllCities ----------
    @Test
    void getAllCities_shouldReturnListFromRepo() {
        City c1 = new City();
        City c2 = new City();
        when(cityRepository.findAll()).thenReturn(List.of(c1, c2));

        List<City> result = cityService.getAllCities();

        assertThat(result).hasSize(2);
        verify(cityRepository, times(1)).findAll();
    }

    // ---------- findByCityCode ----------
    @Test
    void findByCityCode_whenExists_shouldReturnCity() {
        City expected = new City();
        expected.setCityCode("NYC");
        when(cityRepository.findByCityCode("NYC")).thenReturn(Optional.of(expected));

        City result = cityService.findByCityCode("NYC");

        assertThat(result).isSameAs(expected);
        verify(cityRepository, times(1)).findByCityCode("NYC");
    }

    @Test
    void findByCityCode_whenNotExists_shouldThrowResourceNotFoundException() {
        when(cityRepository.findByCityCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.findByCityCode("UNKNOWN"))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Ciudad no encontrada con el codigo: UNKNOWN");
        verify(cityRepository, times(1)).findByCityCode("UNKNOWN");
    }

    // ---------- saveCity ----------
    @Test
    void saveCity_whenCountIsLessThan2_andCityCodeUnique_andLineFree_shouldSave() {
        // Arrange
        City newCity = new City();
        newCity.setCityCode("LAX");
        newCity.setLineNumber(5);
        newCity.setCityName("Los Angeles");

        // count returns 1 (so 1 < 2)
        when(cityRepository.count()).thenReturn(1L);
        // city code does not exist
        when(cityRepository.findByCityCode("LAX")).thenReturn(Optional.empty());
        // findAll returns a list of existing cities with different line numbers
        City existing1 = new City();
        existing1.setLineNumber(3);
        City existing2 = new City();
        existing2.setLineNumber(4);
        when(cityRepository.findAll()).thenReturn(List.of(existing1, existing2));
        // save
        when(cityRepository.save(newCity)).thenReturn(newCity);

        // Act
        City result = cityService.saveCity(newCity);

        // Assert
        assertThat(result).isSameAs(newCity);
        verify(cityRepository, times(1)).count();
        verify(cityRepository, times(1)).findByCityCode("LAX");
        verify(cityRepository, times(1)).findAll();
        verify(cityRepository, times(1)).save(newCity);
    }

    @Test
    void saveCity_whenCountIs2_shouldThrowBusinessException() {
        // Arrange
        City newCity = new City();
        when(cityRepository.count()).thenReturn(2L);

        // Act & Assert
        assertThatThrownBy(() -> cityService.saveCity(newCity))
                .isInstanceOf(businessException.class)
                .hasMessage("Solo se permiten 2 ciudades en el sistema");
        verify(cityRepository, times(1)).count();
        verify(cityRepository, never()).findByCityCode(any());
        verify(cityRepository, never()).findAll();
        verify(cityRepository, never()).save(any());
    }

    @Test
    void saveCity_whenCityCodeAlreadyExists_shouldThrowBusinessException() {
        // Arrange
        City newCity = new City();
        newCity.setCityCode("NYC");
        when(cityRepository.count()).thenReturn(1L);
        when(cityRepository.findByCityCode("NYC")).thenReturn(Optional.of(new City()));

        // Act & Assert
        assertThatThrownBy(() -> cityService.saveCity(newCity))
                .isInstanceOf(businessException.class)
                .hasMessage("El codigo de ciudad ya existe");
        verify(cityRepository, times(1)).count();
        verify(cityRepository, times(1)).findByCityCode("NYC");
        verify(cityRepository, never()).findAll();
        verify(cityRepository, never()).save(any());
    }

    @Test
    void saveCity_whenLineNumberAlreadyUsed_shouldThrowBusinessException() {
        // Arrange
        City newCity = new City();
        newCity.setCityCode("LAX");
        newCity.setLineNumber(5);
        when(cityRepository.count()).thenReturn(1L);
        when(cityRepository.findByCityCode("LAX")).thenReturn(Optional.empty());
        // Existing city with same line number
        City existing = new City();
        existing.setLineNumber(5);
        when(cityRepository.findAll()).thenReturn(List.of(existing));

        // Act & Assert
        assertThatThrownBy(() -> cityService.saveCity(newCity))
                .isInstanceOf(businessException.class)
                .hasMessage("La linea ya se encuentra asignada a otra ciudad");
        verify(cityRepository, times(1)).count();
        verify(cityRepository, times(1)).findByCityCode("LAX");
        verify(cityRepository, times(1)).findAll();
        verify(cityRepository, never()).save(any());
    }

    // ---------- updateCity ----------
    @Test
    void updateCity_whenExists_andLineNotUsedByOther_shouldUpdateAndSave() {
        // Arrange
        Integer id = 1;
        City existing = new City();
        existing.setCityId(id);
        existing.setCityCode("OLD");
        existing.setLineNumber(3);
        existing.setCityName("Old City");
        existing.setPopulation(1000);

        City updatedData = new City();
        updatedData.setCityName("New City");
        updatedData.setCityCode("NEW");
        updatedData.setLineNumber(4);
        updatedData.setPopulation(2000);

        when(cityRepository.findById(id)).thenReturn(Optional.of(existing));
        // findAll returns other cities, none with lineNumber 4 (except itself, but we exclude id)
        City otherCity = new City();
        otherCity.setCityId(2);
        otherCity.setLineNumber(5);
        when(cityRepository.findAll()).thenReturn(List.of(otherCity));
        when(cityRepository.save(existing)).thenReturn(existing);

        // Act
        City result = cityService.updateCity(id, updatedData);

        // Assert
        assertThat(result).isSameAs(existing);
        assertThat(existing.getCityName()).isEqualTo("New City");
        assertThat(existing.getCityCode()).isEqualTo("NEW");
        assertThat(existing.getLineNumber()).isEqualTo(4);
        assertThat(existing.getPopulation()).isEqualTo(2000);
        verify(cityRepository, times(1)).findById(id);
        verify(cityRepository, times(1)).findAll();
        verify(cityRepository, times(1)).save(existing);
    }

    @Test
    void updateCity_whenNotExists_shouldThrowResourceNotFoundException() {
        // Arrange
        Integer id = 99;
        when(cityRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cityService.updateCity(id, new City()))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("No existe una ciudad con el ID: " + id);
        verify(cityRepository, times(1)).findById(id);
        verify(cityRepository, never()).findAll();
        verify(cityRepository, never()).save(any());
    }

    @Test
    void updateCity_whenLineNumberUsedByAnotherCity_shouldThrowBusinessException() {
        // Arrange
        Integer id = 1;
        City existing = new City();
        existing.setCityId(id);
        existing.setLineNumber(3);

        City updatedData = new City();
        updatedData.setLineNumber(5);

        when(cityRepository.findById(id)).thenReturn(Optional.of(existing));
        // Another city with same line number 5
        City anotherCity = new City();
        anotherCity.setCityId(2);
        anotherCity.setLineNumber(5);
        when(cityRepository.findAll()).thenReturn(List.of(anotherCity));

        // Act & Assert
        assertThatThrownBy(() -> cityService.updateCity(id, updatedData))
                .isInstanceOf(businessException.class)
                .hasMessage("No se puede asignar la linea 5. Ya se encuentra vinculada a otra ciudad.");
        verify(cityRepository, times(1)).findById(id);
        verify(cityRepository, times(1)).findAll();
        verify(cityRepository, never()).save(any());
    }

    @Test
    void updateCity_whenLineNumberIsSameAsOwn_shouldNotThrowAndUpdate() {
        // Arrange
        Integer id = 1;
        City existing = new City();
        existing.setCityId(id);
        existing.setLineNumber(5);
        existing.setCityName("Old");
        existing.setCityCode("OLD");
        existing.setPopulation(100);

        City updatedData = new City();
        updatedData.setLineNumber(5); // same as existing
        updatedData.setCityName("New");
        updatedData.setCityCode("NEW");
        updatedData.setPopulation(200);

        when(cityRepository.findById(id)).thenReturn(Optional.of(existing));
        // findAll returns another city with line number 5 (but it's the same id, filtered out)
        City anotherCity = new City();
        anotherCity.setCityId(2);
        anotherCity.setLineNumber(3); // different line
        when(cityRepository.findAll()).thenReturn(List.of(anotherCity));
        when(cityRepository.save(existing)).thenReturn(existing);

        // Act
        City result = cityService.updateCity(id, updatedData);

        // Assert
        assertThat(result).isSameAs(existing);
        assertThat(existing.getLineNumber()).isEqualTo(5);
        assertThat(existing.getCityName()).isEqualTo("New");
        verify(cityRepository, times(1)).findById(id);
        verify(cityRepository, times(1)).findAll();
        verify(cityRepository, times(1)).save(existing);
    }

    // ---------- deleteCity ----------
    @Test
    void deleteCity_shouldCallRepoDeleteById() {
        // Arrange
        Integer id = 5;

        // Act
        cityService.deleteCity(id);

        // Assert
        verify(cityRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(cityRepository);
    }
}