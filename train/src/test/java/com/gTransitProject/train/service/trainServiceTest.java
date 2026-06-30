package com.gTransitProject.train.service;

import com.gTransitProject.train.model.typeTrain;
import com.gTransitProject.train.repo.typeTrainRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class typeTrainServiceTest {

    @Mock
    private typeTrainRepository typeTrainRepository;

    @InjectMocks
    private typeTrainService typeTrainService;

    @Test
    void createTypeTrain_shouldSaveAndReturn() {
        typeTrain input = new typeTrain();
        // no need to set any fields
        typeTrain saved = new typeTrain();
        when(typeTrainRepository.save(input)).thenReturn(saved);

        typeTrain result = typeTrainService.createTypeTrain(input);

        assertThat(result).isSameAs(saved);
        verify(typeTrainRepository, times(1)).save(input);
    }

    @Test
    void getAllTypeTrains_shouldReturnList() {
        typeTrain t1 = new typeTrain();
        typeTrain t2 = new typeTrain();
        when(typeTrainRepository.findAll()).thenReturn(List.of(t1, t2));

        List<typeTrain> result = typeTrainService.getAllTypeTrains();

        assertThat(result).hasSize(2);
        verify(typeTrainRepository, times(1)).findAll();
    }

    @Test
    void getTypeTrainByID_whenExists_shouldReturn() {
        typeTrain expected = new typeTrain();
        when(typeTrainRepository.findById(5)).thenReturn(Optional.of(expected));

        typeTrain result = typeTrainService.getTypeTrainByID(5);

        assertThat(result).isSameAs(expected);
        verify(typeTrainRepository, times(1)).findById(5);
    }

    @Test
    void getTypeTrainByID_whenNotExists_shouldThrowResponseStatusException() {
        when(typeTrainRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> typeTrainService.getTypeTrainByID(99))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND)
                .extracting("reason").isEqualTo("invalid type");
        verify(typeTrainRepository, times(1)).findById(99);
    }
}