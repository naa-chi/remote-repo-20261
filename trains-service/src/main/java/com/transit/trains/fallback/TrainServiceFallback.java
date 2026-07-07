package com.transit.trains.fallback;

import com.transit.trains.dto.response.TrainResponseDTO;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class TrainServiceFallback {

    public TrainResponseDTO getTrainFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for Train ID " + id + ". Error: " + t.getMessage());
        return createDefaultFallbackTrain(id != null ? id : -1L);
    }

    public List<TrainResponseDTO> getTrainsFallbackList(Throwable t) {
        System.err.println("CircuitBreaker triggered for trains list retrieval. Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackTrain(-1L));
    }

    private TrainResponseDTO createDefaultFallbackTrain(Long id) {
        TrainResponseDTO fallbackDto = new TrainResponseDTO();
        fallbackDto.setId(id);
        fallbackDto.setCode("ERROR");
        fallbackDto.setManufacturerId("Unavailable");
        fallbackDto.setEngineId(0L);
        fallbackDto.setCarAmount(0);
        fallbackDto.setCostPerCar(0);
        fallbackDto.setProductionDate(Date.valueOf(LocalDate.now()));

        return fallbackDto;
    }
}