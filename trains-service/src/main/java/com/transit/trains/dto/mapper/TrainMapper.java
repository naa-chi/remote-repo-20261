package com.transit.trains.dto.mapper;

import com.transit.trains.dto.request.TrainRequestDTO;
import com.transit.trains.dto.response.TrainResponseDTO;
import com.transit.trains.model.TrainModel;
import org.springframework.stereotype.Component;

@Component
public class TrainMapper {

    public TrainResponseDTO toResponse(TrainModel model) {
        if (model == null) {
            return null;
        }

        TrainResponseDTO dto = new TrainResponseDTO();
        dto.setId(model.getId());
        dto.setCode(model.getCode());
        dto.setManufacturerId(model.getManufacturerId());
        dto.setEngineId(model.getEngineId());
        dto.setCarAmount(model.getCarAmount());
        dto.setCostPerCar(model.getCostPerCar());
        dto.setProductionDate(model.getProductionDate());

        return dto;
    }

    public TrainModel toEntity(TrainRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        TrainModel model = new TrainModel();
        model.setCode(dto.getCode());
        model.setManufacturerId(dto.getManufacturerId());
        model.setEngineId(dto.getEngineId());
        model.setCarAmount(dto.getCarAmount());
        model.setCostPerCar(dto.getCostPerCar());
        model.setProductionDate(dto.getProductionDate());

        return model;
    }
}