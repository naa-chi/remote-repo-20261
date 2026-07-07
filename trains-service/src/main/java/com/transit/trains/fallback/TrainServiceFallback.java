package com.transit.trains.fallback;

import com.transit.trains.dto.TrainDTO;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.time.LocalDate;

@Component
public class TrainServiceFallback {

    public TrainDTO getTrainFallback(Long id, Throwable t) {
        // Log the error so you know the CircuitBreaker was triggered
        System.err.println("CircuitBreaker triggered for Train ID " + id + ". Error: " + t.getMessage());

        // Create a safe, default fallback DTO using setters
        TrainDTO fallbackDto = new TrainDTO();
        fallbackDto.setId(id != null ? id : -1L);
        fallbackDto.setCode("ERROR");
        fallbackDto.setManufacturerId("Unavailable");
        fallbackDto.setEngineId(0L);
        fallbackDto.setCarAmount(0);
        fallbackDto.setCostPerCar(0);
        fallbackDto.setProductionDate(Date.valueOf(LocalDate.now())); 

        return fallbackDto;
    }
}