package com.transit.trains.assembler;

import com.transit.trains.dto.TrainDTO;
import com.transit.trains.model.TrainModel;
import org.springframework.stereotype.Component;

@Component
public class TrainAssembler {

    public TrainDTO toDTO(TrainModel model) {
        if (model == null) {
            return null;
        }
        
        // Use the empty constructor, then use setters. 
        // This safely ignores the HATEOAS 'links' property during conversion.
        TrainDTO dto = new TrainDTO();
        dto.setId(model.getId());
        dto.setCode(model.getCode());
        dto.setManufacturerId(model.getManufacturerId());
        dto.setEngineId(model.getEngineId());
        dto.setCarAmount(model.getCarAmount());
        dto.setCostPerCar(model.getCostPerCar());
        dto.setProductionDate(model.getProductionDate());
        
        return dto;
    }

    public TrainModel toEntity(TrainDTO dto) {
        if (dto == null) {
            return null;
        }
        
        TrainModel model = new TrainModel();
        model.setId(dto.getId());
        model.setCode(dto.getCode());
        model.setManufacturerId(dto.getManufacturerId());
        model.setEngineId(dto.getEngineId());
        model.setCarAmount(dto.getCarAmount());
        model.setCostPerCar(dto.getCostPerCar());
        model.setProductionDate(dto.getProductionDate());
        
        return model;
    }
}