package com.transit.engines.dto.mapper;

import com.transit.engines.dto.request.EngineRequestDTO;
import com.transit.engines.dto.response.EngineResponseDTO;
import com.transit.engines.model.EngineModel;
import org.springframework.stereotype.Component;

@Component
public class EngineAssembler {

    public EngineResponseDTO toResponse(EngineModel model) {
        if (model == null) return null;

        EngineResponseDTO dto = new EngineResponseDTO();
        dto.setId(model.getId());
        dto.setEngineId(model.getEngineId());
        dto.setManufacturerId(model.getManufacturerId());
        dto.setEngineCode(model.getEngineCode());
        dto.setEngineHorsepower(model.getEngineHorsepower());
        dto.setEngineWeight(model.getEngineWeight());
        dto.setEnginePrice(model.getEnginePrice());
        dto.setProductionDate(model.getProductionDate());

        return dto;
    }

    public EngineModel toEntity(EngineRequestDTO dto) {
        if (dto == null) return null;

        EngineModel model = new EngineModel();
        model.setEngineId(dto.getEngineId());
        model.setManufacturerId(dto.getManufacturerId());
        model.setEngineCode(dto.getEngineCode());
        model.setEngineHorsepower(dto.getEngineHorsepower());
        model.setEngineWeight(dto.getEngineWeight());
        model.setEnginePrice(dto.getEnginePrice());
        model.setProductionDate(dto.getProductionDate());

        return model;
    }
}