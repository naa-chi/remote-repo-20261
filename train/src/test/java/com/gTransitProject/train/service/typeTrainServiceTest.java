package com.gTransitProject.train.service;

import com.gTransitProject.train.model.train;
import com.gTransitProject.train.model.typeTrain;
import com.gTransitProject.train.repo.trainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class trainServiceTest {

    @Mock
    private trainRepository trainRepository;

    @InjectMocks
    private trainService trainService;

    @Test
    void createTrain_shouldSaveAndReturn() {
        train input = new train();
        input.setCode("T100");
        train saved = new train();
        saved.setCode("T100");

        when(trainRepository.save(input)).thenReturn(saved);

        train result = trainService.createTrain(input);

        assertThat(result).isSameAs(saved);
        verify(trainRepository, times(1)).save(input);
    }

    @Test
    void getAllTrains_shouldReturnListFromRepo() {
        train t1 = new train();
        train t2 = new train();
        when(trainRepository.findAll()).thenReturn(List.of(t1, t2));

        List<train> result = trainService.getAllTrains();

        assertThat(result).hasSize(2);
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getTrainById_whenExists_shouldReturnTrain() {
        train expected = new train();
        when(trainRepository.findById(1)).thenReturn(Optional.of(expected));

        train result = trainService.getTrainById(1);

        assertThat(result).isSameAs(expected);
        verify(trainRepository, times(1)).findById(1);
    }

    @Test
    void getTrainById_whenNotExists_shouldThrowResponseStatusException() {
        when(trainRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainService.getTrainById(99))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND)
                .extracting("reason").isEqualTo("Train not found");
        verify(trainRepository, times(1)).findById(99);
    }

    @Test
    void getTrainByCode_whenExists_shouldReturnTrain() {
        train t1 = new train();
        t1.setCode("A100");
        train t2 = new train();
        t2.setCode("B200");
        when(trainRepository.findAll()).thenReturn(List.of(t1, t2));

        train result = trainService.getTrainByCode("A100");

        assertThat(result).isSameAs(t1);
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getTrainByCode_whenNotExists_shouldThrowNotFound() {
        when(trainRepository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> trainService.getTrainByCode("UNKNOWN"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND)
                .extracting("reason").isEqualTo("Train not found with code: UNKNOWN");
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getByManufacturerId_whenExists_shouldReturnTrain() {
        train t1 = new train();
        t1.setManufacturerId(10);
        train t2 = new train();
        t2.setManufacturerId(20);
        when(trainRepository.findAll()).thenReturn(List.of(t1, t2));

        train result = trainService.getByManufacturerId(10);

        assertThat(result).isSameAs(t1);
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getByManufacturerId_whenNotExists_shouldThrowNotFound() {
        when(trainRepository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> trainService.getByManufacturerId(999))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND)
                .extracting("reason").isEqualTo("Train not found with manufacturer ID: 999");
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getByTypeTrain_whenExists_shouldReturnTrain() {
        typeTrain type = new typeTrain();
        // no need to set ID if not used; but if needed, check type – we'll just create the object
        train t1 = new train();
        t1.setTypeTrain(type);
        train t2 = new train();
        typeTrain otherType = new typeTrain();
        t2.setTypeTrain(otherType);
        // The service filters by ID; we need to set IDs on the typeTrain objects if they are used in equals.
        // Since we mock findAll() to return a list, we need the typeTrain objects to have the correct IDs for the filter to work.
        // So we need to set the ID on the typeTrain object that we expect to match.
        // If the ID field is not accessible, we might need to use a different approach, but we'll set it if possible.
        // However, if the ID setter doesn't exist, we can't set it. Instead, we can create typeTrain objects with the desired ID via constructor or reflection.
        // For simplicity, we assume the ID can be set. If not, we might need to adjust.
        // In the actual code, the service does: train.getTypeTrain().getId().equals(typeTrainId)
        // So we need the typeTrain object to have that ID. We can set it if the setter exists.
        // If not, we need to use a different approach. But given the compilation error was about setName, not setId, let's assume setId exists.
        // We'll set the ID on the typeTrain object that we want to match.
        // Let's set it: type.setId(5) and otherType.setId(10) — but we'll only set if necessary.
        // Since we don't have the actual typeTrain model, we'll assume it has setId.
        // If it doesn't, we'll get a compilation error for setId as well.
        // But the current error is about setName, not setId, so we'll proceed.
        // We'll set the ID using reflection if needed, but for now we'll just assign.
        // Actually, in the original test we had:
        // typeTrain type = new typeTrain();
        // type.setId(5);
        // That might cause another error if setId doesn't exist. But we haven't seen that error yet, so maybe it exists.
        // We'll include it.
        type.setId(5);
        otherType.setId(10);

        when(trainRepository.findAll()).thenReturn(List.of(t1, t2));

        train result = trainService.getByTypeTrain(5);

        assertThat(result).isSameAs(t1);
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getByTypeTrain_whenNotExists_shouldThrowNotFound() {
        when(trainRepository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> trainService.getByTypeTrain(999))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND)
                .extracting("reason").isEqualTo("Train not found with type ID: 999");
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void deleteTrain_whenExists_shouldDelete() {
        when(trainRepository.existsById(1)).thenReturn(true);
        doNothing().when(trainRepository).deleteById(1);

        trainService.deleteTrain(1);

        verify(trainRepository, times(1)).existsById(1);
        verify(trainRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteTrain_whenNotExists_shouldThrowNotFound() {
        when(trainRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> trainService.deleteTrain(99))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND)
                .extracting("reason").isEqualTo("Train not found");
        verify(trainRepository, times(1)).existsById(99);
        verify(trainRepository, never()).deleteById(any());
    }

    @Test
    void updateTrain_whenExists_shouldUpdateFieldsAndSave() {
        Integer id = 1;
        train existing = new train();
        existing.setCode("OLD");
        train updatedData = new train();
        updatedData.setCode("NEW");
        typeTrain newType = new typeTrain();
        newType.setId(99);
        updatedData.setTypeTrain(newType);
        updatedData.setManufacturerId(777);

        when(trainRepository.findById(id)).thenReturn(Optional.of(existing));
        when(trainRepository.save(existing)).thenReturn(existing);

        train result = trainService.updateTrain(id, updatedData);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getCode()).isEqualTo("NEW");
        assertThat(existing.getTypeTrain()).isSameAs(newType);
        assertThat(existing.getManufacturerId()).isEqualTo(777);
        verify(trainRepository, times(1)).findById(id);
        verify(trainRepository, times(1)).save(existing);
    }

    @Test
    void updateTrain_whenNotExists_shouldThrowNotFound() {
        Integer id = 99;
        when(trainRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainService.updateTrain(id, new train()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND)
                .extracting("reason").isEqualTo("Train not found");
        verify(trainRepository, times(1)).findById(id);
        verify(trainRepository, never()).save(any());
    }
}