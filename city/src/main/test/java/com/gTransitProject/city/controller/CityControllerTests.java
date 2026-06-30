package com.gTransitProject.city.controller;

import com.gTransitProject.city.model.City;
import com.gTransitProject.city.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    @Mock
    private CityService cityService;

    @Mock
    private CityAssembler assembler;

    @InjectMocks
    private CityController controller;

    private City sampleCity;
    private EntityModel<City> sampleModel;

    @BeforeEach
    void setUp() {
        sampleCity = new City();
        sampleCity.setCityId(1);
        sampleCity.setCityName("Los Angeles");
        sampleCity.setCityCode("LAX");
        sampleCity.setLineNumber(7);
        sampleCity.setPopulation(4000000);

        sampleModel = EntityModel.of(sampleCity);
    }

    @Test
    void getAllCities_shouldReturnCollectionModelWithSelfLink() {
        List<City> cities = Arrays.asList(sampleCity, new City());
        CollectionModel<EntityModel<City>> collection = CollectionModel.of(
                Arrays.asList(EntityModel.of(cities.get(0)), EntityModel.of(cities.get(1))));
        when(cityService.getAllCities()).thenReturn(cities);
        when(assembler.toCollectionModel(cities)).thenReturn(collection);

        CollectionModel<EntityModel<City>> result = controller.getAllCities();

        assertEquals(2, result.getContent().size());
        verify(cityService, times(1)).getAllCities();
        verify(assembler, times(1)).toCollectionModel(cities);
    }

    @Test
    void getCityByCode_withValidCode_shouldReturnOkResponseWithModel() {
        String code = "LAX";
        when(cityService.findByCityCode(code)).thenReturn(sampleCity);
        when(assembler.toModel(sampleCity)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<City>> response = controller.getCityByCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(cityService, times(1)).findByCityCode(code);
        verify(assembler, times(1)).toModel(sampleCity);
    }

    @Test
    void getCityByCode_withInvalidCode_shouldReturnNotFound() {
        String code = "INVALID";
        when(cityService.findByCityCode(code)).thenReturn(null);

        ResponseEntity<EntityModel<City>> response = controller.getCityByCode(code);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(cityService, times(1)).findByCityCode(code);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void createCity_shouldReturnCreatedResponseWithModel() {
        when(cityService.saveCity(any(City.class))).thenReturn(sampleCity);
        when(assembler.toModel(sampleCity)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<City>> response = controller.createCity(sampleCity);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(cityService, times(1)).saveCity(sampleCity);
        verify(assembler, times(1)).toModel(sampleCity);
    }

    @Test
    void updateCity_withValidId_shouldReturnOkResponseWithModel() {
        Integer id = 1;
        City updated = new City();
        updated.setCityId(1);
        updated.setCityName("New York");
        when(cityService.updateCity(eq(id), any(City.class))).thenReturn(updated);
        when(assembler.toModel(updated)).thenReturn(EntityModel.of(updated));

        ResponseEntity<EntityModel<City>> response = controller.updateCity(id, sampleCity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EntityModel.of(updated), response.getBody());
        verify(cityService, times(1)).updateCity(id, sampleCity);
        verify(assembler, times(1)).toModel(updated);
    }

    @Test
    void updateCity_withInvalidId_shouldReturnNotFound() {
        Integer id = 999;
        when(cityService.updateCity(eq(id), any(City.class))).thenReturn(null);

        ResponseEntity<EntityModel<City>> response = controller.updateCity(id, sampleCity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(cityService, times(1)).updateCity(id, sampleCity);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void deleteCity_shouldReturnNoContent() {
        Integer id = 1;
        doNothing().when(cityService).deleteCity(id);

        ResponseEntity<Void> response = controller.deleteCity(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(cityService, times(1)).deleteCity(id);
    }
}