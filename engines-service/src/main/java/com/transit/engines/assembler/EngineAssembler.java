package com.transit.engines.assembler;

import com.transit.engines.dto.EngineDTO;
import com.transit.engines.model.EngineModel;
import org.springframework.stereotype.Component;

@Component
public class EngineAssembler {
    public EngineDTO toDTO(EngineModel model) {
        if (model == null) {
            return null;
        }

        EngineDTO dto = new EngineDTO();
        dto.setId(model.getId());
        dto.setManufacturerId(model.getManufacturerId());
        dto.setEngineCode(model.getEngineCode());
        dto.setEngineHorsepower(model.getEngineHorsepower());
        dto.setEngineWeight(model.getEngineWeight());
        dto.setEnginePrice(model.getEnginePrice());
        dto.setProductionDate(model.getProductionDate());

        return dto;
    }

    public EngineModel toEntity(EngineDTO dto) {
        if (dto == null) {
            return null;
        }

        EngineModel model = new EngineModel();
        model.setId(dto.getId());
        model.setManufacturerId(dto.getManufacturerId());
        model.setEngineCode(dto.getEngineCode());
        model.setEngineHorsepower(dto.getEngineHorsepower());
        model.setEngineWeight(dto.getEngineWeight());
        model.setEnginePrice(dto.getEnginePrice());
        model.setProductionDate(dto.getProductionDate());

        return model;
    }
}
