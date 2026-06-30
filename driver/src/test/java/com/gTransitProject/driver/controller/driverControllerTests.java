package com.gTransitProject.driver.controller;

import com.gTransitProject.driver.model.driverModel;
import com.gTransitProject.driver.service.driverService;
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
class driverControllerTest {

    @Mock
    private driverService driverService;

    @Mock
    private DriverAssembler assembler;

    @InjectMocks
    private driverController controller;

    private driverModel sampleDriver;
    private EntityModel<driverModel> sampleModel;

    @BeforeEach
    void setUp() {
        sampleDriver = new driverModel();
        sampleDriver.setDriverId(1);
        sampleDriver.setDriverName("John Doe");
        sampleDriver.setDriverLicenseNumber("LIC123456");
        sampleDriver.setDriverContactNumber("555-1234");
        sampleDriver.setMonthlyWage(5000);
        sampleDriver.setExpenses(1000);

        sampleModel = EntityModel.of(sampleDriver);
    }

    @Test
    void getAllDrivers_shouldReturnCollectionModelWithSelfLink() {
        List<driverModel> drivers = Arrays.asList(sampleDriver, new driverModel());
        CollectionModel<EntityModel<driverModel>> collection = CollectionModel.of(
                Arrays.asList(EntityModel.of(drivers.get(0)), EntityModel.of(drivers.get(1))));
        when(driverService.getAllDrivers()).thenReturn(drivers);
        when(assembler.toCollectionModel(drivers)).thenReturn(collection);

        CollectionModel<EntityModel<driverModel>> result = controller.getAllDrivers();

        assertEquals(2, result.getContent().size());
        verify(driverService, times(1)).getAllDrivers();
        verify(assembler, times(1)).toCollectionModel(drivers);
    }

    @Test
    void createDriver_shouldReturnCreatedResponseWithModel() {
        when(driverService.createDriver(any(driverModel.class))).thenReturn(sampleDriver);
        when(assembler.toModel(sampleDriver)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<driverModel>> response = controller.createDriver(sampleDriver);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(driverService, times(1)).createDriver(sampleDriver);
        verify(assembler, times(1)).toModel(sampleDriver);
    }

    @Test
    void getDriverById_withValidId_shouldReturnOkResponseWithModel() {
        Long id = 1L;
        when(driverService.getDriverById(id)).thenReturn(sampleDriver);
        when(assembler.toModel(sampleDriver)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<driverModel>> response = controller.getDriverById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(driverService, times(1)).getDriverById(id);
        verify(assembler, times(1)).toModel(sampleDriver);
    }

    @Test
    void getDriverById_withInvalidId_shouldReturnNotFound() {
        Long id = 999L;
        when(driverService.getDriverById(id)).thenReturn(null);

        ResponseEntity<EntityModel<driverModel>> response = controller.getDriverById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(driverService, times(1)).getDriverById(id);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void updateDriver_shouldReturnOkResponseWithUpdatedModel() {
        Long id = 1L;
        driverModel updated = new driverModel();
        updated.setDriverId(1);
        updated.setDriverName("Jane Doe");
        when(driverService.updateDriver(eq(id), any(driverModel.class))).thenReturn(updated);
        when(assembler.toModel(updated)).thenReturn(EntityModel.of(updated));

        ResponseEntity<EntityModel<driverModel>> response = controller.updateDriver(id, sampleDriver);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EntityModel.of(updated), response.getBody());
        verify(driverService, times(1)).updateDriver(id, sampleDriver);
        verify(assembler, times(1)).toModel(updated);
    }

    @Test
    void updateDriver_withInvalidId_shouldReturnNotFound() {
        Long id = 999L;
        when(driverService.updateDriver(eq(id), any(driverModel.class))).thenReturn(null);

        ResponseEntity<EntityModel<driverModel>> response = controller.updateDriver(id, sampleDriver);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(driverService, times(1)).updateDriver(id, sampleDriver);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void deleteDriver_shouldReturnNoContent() {
        Long id = 1L;
        doNothing().when(driverService).deleteDriver(id);

        ResponseEntity<Void> response = controller.deleteDriver(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(driverService, times(1)).deleteDriver(id);
    }
}