package com.gTransitProject.city.controller;

import com.gTransitProject.city.model.City;
import com.gTransitProject.city.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController controller;

    private City sampleCity;

    @BeforeEach
    void setUp() {
        sampleCity = new City();
        sampleCity.setCityId(1);
        sampleCity.setCityName("Los Angeles");
        sampleCity.setCityCode("LAX");
        sampleCity.setLineNumber(7);
        sampleCity.setPopulation(4000000);
    }

    @Test
    void getAllCities_shouldReturnListOfCities() {
        List<City> cities = Arrays.asList(sampleCity, new City());
        when(cityService.getAllCities()).thenReturn(cities);

        ResponseEntity<List<City>> response = controller.getAllCities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(cityService, times(1)).getAllCities();
    }

    @Test
    void getCityByCode_withValidCode_shouldReturnCity() {
        String code = "LAX";
        when(cityService.findByCityCode(code)).thenReturn(sampleCity);

        ResponseEntity<City> response = controller.getCityByCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleCity, response.getBody());
        verify(cityService, times(1)).findByCityCode(code);
    }

    @Test
    void getCityByCode_withInvalidCode_shouldReturnNull() {
        String code = "INVALID";
        when(cityService.findByCityCode(code)).thenReturn(null);

        ResponseEntity<City> response = controller.getCityByCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(cityService, times(1)).findByCityCode(code);
    }

    @Test
    void createCity_shouldReturnCreatedCityWithStatus201() {
        when(cityService.saveCity(any(City.class))).thenReturn(sampleCity);

        ResponseEntity<City> response = controller.createCity(sampleCity);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleCity, response.getBody());
        verify(cityService, times(1)).saveCity(sampleCity);
    }

    @Test
    void updateCity_withValidId_shouldReturnUpdatedCity() {
        Integer id = 1;
        City updated = new City();
        updated.setCityId(1);
        updated.setCityName("New York");
        when(cityService.updateCity(eq(id), any(City.class))).thenReturn(updated);

        ResponseEntity<City> response = controller.updateCity(id, sampleCity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(cityService, times(1)).updateCity(id, sampleCity);
    }

    @Test
    void updateCity_withInvalidId_shouldReturnNull() {
        Integer id = 999;
        when(cityService.updateCity(eq(id), any(City.class))).thenReturn(null);

        ResponseEntity<City> response = controller.updateCity(id, sampleCity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(cityService, times(1)).updateCity(id, sampleCity);
    }

    @Test
    void deleteCity_shouldReturnSuccessMessage() {
        Integer id = 1;
        doNothing().when(cityService).deleteCity(id);

        ResponseEntity<String> response = controller.deleteCity(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ciudad eliminada correctamente", response.getBody());
        verify(cityService, times(1)).deleteCity(id);
    }
}