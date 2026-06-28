package test.java.com.gTransitProject.train.controller; //Why is this different

import com.gTransitProject.train.model.train;
import com.gTransitProject.train.model.typeTrain;
import com.gTransitProject.train.service.trainService;
import com.gTransitProject.train.controller.trainController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class trainControllerTests {

    @Mock
    private trainService trainService;

    @InjectMocks
    private trainController controller;

    private train sampleTrain;

    @BeforeEach
    void setUp() {
        sampleTrain = new train();
        sampleTrain.setSpecificTrainId(1);
        sampleTrain.setCode("TR123");
        sampleTrain.setManufacturerId(10);
        sampleTrain.setEngineTypeId(5);
        sampleTrain.setCarAmount(4);
        sampleTrain.setManufacturingDate(Date.valueOf("2025-01-01"));
        sampleTrain.setPricePerCar(100000);
        // typeTrain can be set if needed, but not required for tests
    }

    @Test
    void createTrain_shouldReturnCreatedTrainWithStatus201() {
        when(trainService.createTrain(any(train.class))).thenReturn(sampleTrain);

        ResponseEntity<train> response = controller.createTrain(sampleTrain);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleTrain, response.getBody());
        verify(trainService, times(1)).createTrain(sampleTrain);
    }

    @Test
    void getAll_shouldReturnListOfTrains() {
        List<train> trains = Arrays.asList(sampleTrain, new train());
        when(trainService.getAllTrains()).thenReturn(trains);

        List<train> result = controller.getAll();

        assertEquals(2, result.size());
        verify(trainService, times(1)).getAllTrains();
    }

    @Test
    void getByCode_withValidCode_shouldReturnTrain() {
        String code = "TR123";
        when(trainService.getTrainByCode(code)).thenReturn(sampleTrain);

        ResponseEntity<train> response = controller.getByCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleTrain, response.getBody());
        verify(trainService, times(1)).getTrainByCode(code);
    }

    @Test
    void getByCode_withInvalidCode_shouldReturnNull() {
        String code = "INVALID";
        when(trainService.getTrainByCode(code)).thenReturn(null);

        ResponseEntity<train> response = controller.getByCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(trainService, times(1)).getTrainByCode(code);
    }

    @Test
    void getByManufacturerId_withValidId_shouldReturnTrain() {
        Integer manufacturerId = 10;
        when(trainService.getByManufacturerId(manufacturerId)).thenReturn(sampleTrain);

        ResponseEntity<train> response = controller.getByManufacturerId(manufacturerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleTrain, response.getBody());
        verify(trainService, times(1)).getByManufacturerId(manufacturerId);
    }

    @Test
    void getByManufacturerId_withInvalidId_shouldReturnNull() {
        Integer manufacturerId = 999;
        when(trainService.getByManufacturerId(manufacturerId)).thenReturn(null);

        ResponseEntity<train> response = controller.getByManufacturerId(manufacturerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(trainService, times(1)).getByManufacturerId(manufacturerId);
    }

    @Test
    void getByTypeTrain_withValidTypeId_shouldReturnTrain() {
        Integer typeId = 1;
        when(trainService.getByTypeTrain(typeId)).thenReturn(sampleTrain);

        ResponseEntity<train> response = controller.getByTypeTrain(typeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleTrain, response.getBody());
        verify(trainService, times(1)).getByTypeTrain(typeId);
    }

    @Test
    void getByTypeTrain_withInvalidTypeId_shouldReturnNull() {
        Integer typeId = 999;
        when(trainService.getByTypeTrain(typeId)).thenReturn(null);

        ResponseEntity<train> response = controller.getByTypeTrain(typeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(trainService, times(1)).getByTypeTrain(typeId);
    }

    @Test
    void getById_deprecatedMethod_shouldReturnTrain() {
        Integer id = 1;
        when(trainService.getTrainById(id)).thenReturn(sampleTrain);

        ResponseEntity<train> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleTrain, response.getBody());
        verify(trainService, times(1)).getTrainById(id);
    }

    @Test
    void updateTrain_shouldReturnUpdatedTrain() {
        Integer id = 1;
        train updated = new train();
        updated.setSpecificTrainId(1);
        updated.setCarAmount(5);
        when(trainService.updateTrain(eq(id), any(train.class))).thenReturn(updated);

        ResponseEntity<train> response = controller.updateTrain(id, sampleTrain);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(trainService, times(1)).updateTrain(id, sampleTrain);
    }

    @Test
    void deleteTrain_shouldReturnNoContent() {
        Integer id = 1;
        doNothing().when(trainService).deleteTrain(id);

        ResponseEntity<Void> response = controller.deleteTrain(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(trainService, times(1)).deleteTrain(id);
    }
}