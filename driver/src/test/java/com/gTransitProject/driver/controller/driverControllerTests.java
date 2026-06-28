package com.gTransitProject.driver.controller;

import com.gTransitProject.driver.model.driverModel;
import com.gTransitProject.driver.service.driverService;
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
class driverControllerTest {

    @Mock
    private driverService driverService;

    @InjectMocks
    private driverController controller;

    private driverModel sampleDriver;

    @BeforeEach
    void setUp() {
        sampleDriver = new driverModel();
        sampleDriver.setDriverId(1);
        sampleDriver.setDriverName("John Doe");
        sampleDriver.setDriverLicenseNumber("LIC123456");
        sampleDriver.setDriverContactNumber("555-1234");
        sampleDriver.setMonthlyWage(5000);
        sampleDriver.setExpenses(1000);
    }

    @Test
    void getAllDrivers_shouldReturnListOfDrivers() {
        List<driverModel> drivers = Arrays.asList(sampleDriver, new driverModel());
        when(driverService.getAllDrivers()).thenReturn(drivers);

        ResponseEntity<List<driverModel>> response = controller.getAllDrivers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(driverService, times(1)).getAllDrivers();
    }

    @Test
    void createDriver_shouldReturnCreatedDriver() {
        when(driverService.createDriver(any(driverModel.class))).thenReturn(sampleDriver);

        ResponseEntity<driverModel> response = controller.createDriver(sampleDriver);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleDriver, response.getBody());
        verify(driverService, times(1)).createDriver(sampleDriver);
    }

    @Test
    void getDriverById_withValidId_shouldReturnDriver() {
        Long id = 1L;
        when(driverService.getDriverById(id)).thenReturn(sampleDriver);

        ResponseEntity<driverModel> response = controller.getDriverById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleDriver, response.getBody());
        verify(driverService, times(1)).getDriverById(id);
    }

    @Test
    void getDriverById_withInvalidId_shouldReturnNull() {
        Long id = 999L;
        when(driverService.getDriverById(id)).thenReturn(null);

        ResponseEntity<driverModel> response = controller.getDriverById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(driverService, times(1)).getDriverById(id);
    }

    @Test
    void updateDriver_shouldReturnUpdatedDriver() {
        Long id = 1L;
        driverModel updated = new driverModel();
        updated.setDriverId(1);
        updated.setDriverName("Jane Doe");
        when(driverService.updateDriver(eq(id), any(driverModel.class))).thenReturn(updated);

        ResponseEntity<driverModel> response = controller.updateDriver(id, sampleDriver);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(driverService, times(1)).updateDriver(id, sampleDriver);
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