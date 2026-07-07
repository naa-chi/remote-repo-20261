package com.transit.trains.service;

import com.transit.trains.assembler.TrainAssembler;
import com.transit.trains.dto.TrainDTO;
import com.transit.trains.fallback.TrainServiceFallback;
import com.transit.trains.model.TrainModel;
import com.transit.trains.repository.TrainsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainServiceTest {

    @Mock
    private TrainsRepository repository;

    @Mock
    private TrainAssembler assembler;

    @Mock
    private TrainServiceFallback fallback; // Required by constructor

    @InjectMocks
    private TrainService service;

    @Test
    void getTrainById_ShouldReturnTrain_WhenExists() {
        // Arrange
        Long id = 1L;
        TrainModel model = new TrainModel();
        TrainDTO dto = new TrainDTO();
        when(repository.findById(id)).thenReturn(Optional.of(model));
        when(assembler.toDTO(model)).thenReturn(dto);

        // Act
        TrainDTO result = service.getTrainById(id);

        // Assert
        assertNotNull(result);
        verify(repository).findById(id);
    }

    @Test
    void getTrainById_ShouldThrowException_WhenNotFound() {
        // Arrange
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getTrainById(id));
        assertEquals("Train not found with id: " + id, exception.getMessage());
    }

    @Test
    void createTrain_ShouldSaveAndReturnDTO() {
        // Arrange
        TrainDTO inputDto = new TrainDTO();
        TrainModel entity = new TrainModel();
        TrainModel savedEntity = new TrainModel();
        TrainDTO outputDto = new TrainDTO();

        when(assembler.toEntity(inputDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(assembler.toDTO(savedEntity)).thenReturn(outputDto);

        // Act
        TrainDTO result = service.createTrain(inputDto);

        // Assert
        assertNotNull(result);
        verify(repository).save(entity);
    }
}