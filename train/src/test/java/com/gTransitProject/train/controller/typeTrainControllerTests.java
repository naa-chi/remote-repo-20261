package com.gTransitProject.train.controller;

import com.gTransitProject.train.model.typeTrain;
import com.gTransitProject.train.service.typeTrainService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class typeTrainControllerTests {

    @Mock
    private typeTrainService typeTrainService;

    @Mock
    private typeTrainAssembler assembler;

    @InjectMocks
    private typeTrainController controller;

    private typeTrain sampleTypeTrain;
    private EntityModel<typeTrain> sampleModel;

    @BeforeEach
    void setUp() {
        sampleTypeTrain = new typeTrain();
        sampleTypeTrain.setId(1);
        sampleTypeTrain.setType("Passenger");
        sampleTypeTrain.setTypeCode(10);

        sampleModel = EntityModel.of(sampleTypeTrain);
    }

    @Test
    void createTypeTrain_shouldReturnCreatedResponseWithModel() {
        when(typeTrainService.createTypeTrain(any(typeTrain.class))).thenReturn(sampleTypeTrain);
        when(assembler.toModel(sampleTypeTrain)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<typeTrain>> response = controller.createTypeTrain(sampleTypeTrain);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(typeTrainService, times(1)).createTypeTrain(sampleTypeTrain);
        verify(assembler, times(1)).toModel(sampleTypeTrain);
    }

    @Test
    void getAll_shouldReturnCollectionModelWithSelfLink() {
        List<typeTrain> typeTrains = Arrays.asList(sampleTypeTrain, new typeTrain());
        CollectionModel<EntityModel<typeTrain>> collection = CollectionModel.of(
                Arrays.asList(EntityModel.of(typeTrains.get(0)), EntityModel.of(typeTrains.get(1))));
        when(typeTrainService.getAllTypeTrains()).thenReturn(typeTrains);
        when(assembler.toCollectionModel(typeTrains)).thenReturn(collection);

        CollectionModel<EntityModel<typeTrain>> result = controller.getAll();

        assertEquals(2, result.getContent().size());
        verify(typeTrainService, times(1)).getAllTypeTrains();
        verify(assembler, times(1)).toCollectionModel(typeTrains);
    }

    @Test
    void getById_withValidId_shouldReturnOkResponseWithModel() {
        Integer id = 1;
        when(typeTrainService.getTypeTrainByID(id)).thenReturn(sampleTypeTrain);
        when(assembler.toModel(sampleTypeTrain)).thenReturn(sampleModel);

        ResponseEntity<EntityModel<typeTrain>> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
        verify(typeTrainService, times(1)).getTypeTrainByID(id);
        verify(assembler, times(1)).toModel(sampleTypeTrain);
    }

    @Test
    void getById_withInvalidId_shouldReturnNotFound() {
        Integer id = 999;
        when(typeTrainService.getTypeTrainByID(id)).thenReturn(null);

        ResponseEntity<EntityModel<typeTrain>> response = controller.getById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(typeTrainService, times(1)).getTypeTrainByID(id);
        verify(assembler, never()).toModel(any());
    }
}