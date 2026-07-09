package com.transit.manufacturers.dto.mapper;

import com.transit.manufacturers.dto.request.ManufacturerRequestDTO;
import com.transit.manufacturers.dto.response.ManufacturerResponseDTO;
import com.transit.manufacturers.model.ManufacturerModel;
import org.springframework.stereotype.Component;

@Component
public class ManufacturerMapper {

    public ManufacturerResponseDTO toResponse(ManufacturerModel model) {
        if (model == null) return null;
        ManufacturerResponseDTO dto = new ManufacturerResponseDTO();
        dto.setId(model.getId());
        dto.setManufacturerId(model.getManufacturerId());
        dto.setCountryOfOrigin(model.getCountryOfOrigin());
        dto.setFoundingDate(model.getFoundingDate());
        dto.setRevenue(model.getRevenue());
        return dto;
    }

    public ManufacturerModel toEntity(ManufacturerRequestDTO dto) {
        if (dto == null) return null;
        ManufacturerModel model = new ManufacturerModel();
        model.setManufacturerId(dto.getManufacturerId());
        model.setCountryOfOrigin(dto.getCountryOfOrigin());
        model.setFoundingDate(dto.getFoundingDate());
        model.setRevenue(dto.getRevenue());
        return model;
    }
}