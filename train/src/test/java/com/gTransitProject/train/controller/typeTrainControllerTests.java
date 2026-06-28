package test.java.com.gTransitProject.train.controller;

import com.gTransitProject.train.model.typeTrain;
import com.gTransitProject.train.service.typeTrainService;
import com.gTransitProject.train.controller.typeTrainController;
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
class typeTrainControllerTests {

    @Mock
    private typeTrainService typeTrainService;

    @InjectMocks
    private typeTrainController controller;

    private typeTrain sampleTypeTrain;

    @BeforeEach
    void setUp() {
        sampleTypeTrain = new typeTrain();
        sampleTypeTrain.setId(1);
        sampleTypeTrain.setType("Passenger");
        sampleTypeTrain.setTypeCode(15);
    }

    @Test
    void createTypeTrain_shouldReturnCreatedTypeTrainWithStatus201() {
        when(typeTrainService.createTypeTrain(any(typeTrain.class))).thenReturn(sampleTypeTrain);

        ResponseEntity<typeTrain> response = controller.createTypeTrain(sampleTypeTrain);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleTypeTrain, response.getBody());
        verify(typeTrainService, times(1)).createTypeTrain(sampleTypeTrain);
    }

    @Test
    void getAll_shouldReturnListOfTypeTrains() {
        List<typeTrain> typeTrains = Arrays.asList(sampleTypeTrain, new typeTrain());
        when(typeTrainService.getAllTypeTrains()).thenReturn(typeTrains);

        List<typeTrain> result = controller.getAll();

        assertEquals(2, result.size());
        verify(typeTrainService, times(1)).getAllTypeTrains();
    }

    @Test
    void getById_withValidId_shouldReturnTypeTrain() {
        Integer id = 1;
        when(typeTrainService.getTypeTrainByID(id)).thenReturn(sampleTypeTrain);

        ResponseEntity<typeTrain> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleTypeTrain, response.getBody());
        verify(typeTrainService, times(1)).getTypeTrainByID(id);
    }

    @Test
    void getById_withInvalidId_shouldReturnNull() {
        Integer id = 999;
        when(typeTrainService.getTypeTrainByID(id)).thenReturn(null);

        ResponseEntity<typeTrain> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(typeTrainService, times(1)).getTypeTrainByID(id);
    }
}