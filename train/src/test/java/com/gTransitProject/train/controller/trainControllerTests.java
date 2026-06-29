package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.train;
import com.gTransitProject.train.service.trainService;
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

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class trainControllerTests {

    @Mock
    private trainService trainService;

    @Mock
    private trainAssembler assembler;

    @InjectMocks
    private trainController controller;

    private train sampleTrain;
    private EntityModel<train> sampleModel;

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

        sampleModel = EntityModel.of(sampleTrain);
    }

    @Test
    void createTrain_shouldReturnCreatedResponseWithModel() {
        when(trainService.createTrain(any(train.class))).thenReturn(sampleTrain);
        when(assembler.toModel(sampleTrain)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<train>> response = controller.createTrain(sampleTrain);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(trainService, times(1)).createTrain(sampleTrain);
        verify(assembler, times(1)).toModel(sampleTrain);
    }

    @Test
    void getAll_shouldReturnCollectionModelWithSelfLink() {
        List<train> trains = Arrays.asList(sampleTrain, new train());
        CollectionModel<EntityModel<train>> collection = CollectionModel.of(
                Arrays.asList(EntityModel.of(trains.get(0)), EntityModel.of(trains.get(1))));
        when(trainService.getAllTrains()).thenReturn(trains);
        when(assembler.toCollectionModel(trains)).thenReturn(collection);

        CollectionModel<EntityModel<train>> result = controller.getAll();

        assertEquals(2, result.getContent().size());
        verify(trainService, times(1)).getAllTrains();
        verify(assembler, times(1)).toCollectionModel(trains);
    }

    @Test
    void getTrainById_withValidId_shouldReturnOkResponseWithModel() {
        Integer id = 1;
        when(trainService.getTrainById(id)).thenReturn(sampleTrain);
        when(assembler.toModel(sampleTrain)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<train>> response = controller.getTrainById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(trainService, times(1)).getTrainById(id);
        verify(assembler, times(1)).toModel(sampleTrain);
    }

    @Test
    void getTrainById_withInvalidId_shouldReturnNotFound() {
        Integer id = 999;
        when(trainService.getTrainById(id)).thenReturn(null);

        ResponseEntity<EntityModel<train>> response = controller.getTrainById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(trainService, times(1)).getTrainById(id);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void getByCode_withValidCode_shouldReturnOkResponseWithModel() {
        String code = "TR123";
        when(trainService.getTrainByCode(code)).thenReturn(sampleTrain);
        when(assembler.toModel(sampleTrain)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<train>> response = controller.getByCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(trainService, times(1)).getTrainByCode(code);
        verify(assembler, times(1)).toModel(sampleTrain);
    }

    @Test
    void getByCode_withInvalidCode_shouldReturnNotFound() {
        String code = "INVALID";
        when(trainService.getTrainByCode(code)).thenReturn(null);

        ResponseEntity<EntityModel<train>> response = controller.getByCode(code);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(trainService, times(1)).getTrainByCode(code);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void getByManufacturerId_withValidId_shouldReturnOkResponseWithModel() {
        Integer manufacturerId = 10;
        when(trainService.getByManufacturerId(manufacturerId)).thenReturn(sampleTrain);
        when(assembler.toModel(sampleTrain)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<train>> response = controller.getByManufacturerId(manufacturerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(trainService, times(1)).getByManufacturerId(manufacturerId);
        verify(assembler, times(1)).toModel(sampleTrain);
    }

    @Test
    void getByManufacturerId_withInvalidId_shouldReturnNotFound() {
        Integer manufacturerId = 999;
        when(trainService.getByManufacturerId(manufacturerId)).thenReturn(null);

        ResponseEntity<EntityModel<train>> response = controller.getByManufacturerId(manufacturerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(trainService, times(1)).getByManufacturerId(manufacturerId);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void getByTypeTrain_withValidTypeId_shouldReturnOkResponseWithModel() {
        Integer typeId = 1;
        when(trainService.getByTypeTrain(typeId)).thenReturn(sampleTrain);
        when(assembler.toModel(sampleTrain)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<train>> response = controller.getByTypeTrain(typeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(trainService, times(1)).getByTypeTrain(typeId);
        verify(assembler, times(1)).toModel(sampleTrain);
    }

    @Test
    void getByTypeTrain_withInvalidTypeId_shouldReturnNotFound() {
        Integer typeId = 999;
        when(trainService.getByTypeTrain(typeId)).thenReturn(null);

        ResponseEntity<EntityModel<train>> response = controller.getByTypeTrain(typeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(trainService, times(1)).getByTypeTrain(typeId);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void updateTrain_shouldReturnOkResponseWithUpdatedModel() {
        Integer id = 1;
        train updated = new train();
        updated.setSpecificTrainId(1);
        updated.setCarAmount(5);
        when(trainService.updateTrain(eq(id), any(train.class))).thenReturn(updated);
        when(assembler.toModel(updated)).thenReturn(EntityModel.of(updated));

        ResponseEntity<EntityModel<train>> response = controller.updateTrain(id, sampleTrain);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EntityModel.of(updated), response.getBody());
        verify(trainService, times(1)).updateTrain(id, sampleTrain);
        verify(assembler, times(1)).toModel(updated);
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