package com.transit.stations.dto.mapper;

import com.transit.stations.dto.request.StationRequestDTO;
import com.transit.stations.dto.response.StationResponseDTO;
import com.transit.stations.model.StationModel;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public StationResponseDTO toResponse(StationModel model) {
        if (model == null) return null;
        StationResponseDTO dto = new StationResponseDTO();
        dto.setId(model.getId());
        dto.setStationCode(model.getStationCode());
        dto.setCityCode(model.getCityCode());
        dto.setLineCode1(model.getLineCode1());
        dto.setLineCode2(model.getLineCode2());
        dto.setLineCode3(model.getLineCode3());
        dto.setLineCode4(model.getLineCode4());
        return dto;
    }

    public StationModel toEntity(StationRequestDTO dto) {
        if (dto == null) return null;
        StationModel model = new StationModel();
        model.setStationCode(dto.getStationCode());
        model.setCityCode(dto.getCityCode());
        model.setLineCode1(dto.getLineCode1());
        model.setLineCode2(dto.getLineCode2());
        model.setLineCode3(dto.getLineCode3());
        model.setLineCode4(dto.getLineCode4());
        return model;
    }
}