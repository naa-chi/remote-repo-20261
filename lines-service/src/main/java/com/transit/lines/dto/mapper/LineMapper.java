package com.transit.lines.dto.mapper;

import com.transit.lines.dto.request.LineRequestDTO;
import com.transit.lines.dto.response.LineResponseDTO;
import com.transit.lines.model.LineModel;
import org.springframework.stereotype.Component;

@Component
public class LineMapper {

    public LineResponseDTO toResponse(LineModel model) {
        if (model == null) return null;
        LineResponseDTO dto = new LineResponseDTO();
        dto.setId(model.getId());
        dto.setLineCode(model.getLineCode());
        dto.setLineLengthKM(model.getLineLengthKM());
        dto.setPeopleServedMonthlyEstimate(model.getPeopleServedMonthlyEstimate());
        return dto;
    }

    public LineModel toEntity(LineRequestDTO dto) {
        if (dto == null) return null;
        LineModel model = new LineModel();
        model.setLineCode(dto.getLineCode());
        model.setLineLengthKM(dto.getLineLengthKM());
        model.setPeopleServedMonthlyEstimate(dto.getPeopleServedMonthlyEstimate());
        return model;
    }
}