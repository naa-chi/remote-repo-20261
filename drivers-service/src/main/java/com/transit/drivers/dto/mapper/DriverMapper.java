package com.transit.drivers.dto.mapper;

import com.transit.drivers.dto.request.DriverRequestDTO;
import com.transit.drivers.dto.response.DriverResponseDTO;
import com.transit.drivers.model.DriverModel;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public DriverResponseDTO toResponse(DriverModel model) {
        if (model == null) return null;
        DriverResponseDTO dto = new DriverResponseDTO();
        dto.setId(model.getId());
        dto.setCode(model.getCode());
        dto.setSalary(model.getSalary());
        dto.setContractDate(model.getContractDate());
        dto.setDateOfBirth(model.getDateOfBirth());
        dto.setFirstName(model.getFirstName());
        dto.setSecondName(model.getSecondName());
        dto.setCapacitatedCode(model.getCapacitatedCode());
        return dto;
    }

    public DriverModel toEntity(DriverRequestDTO dto) {
        if (dto == null) return null;
        DriverModel model = new DriverModel();
        model.setCode(dto.getCode());
        model.setSalary(dto.getSalary());
        model.setContractDate(dto.getContractDate());
        model.setDateOfBirth(dto.getDateOfBirth());
        model.setFirstName(dto.getFirstName());
        model.setSecondName(dto.getSecondName());
        model.setCapacitatedCode(dto.getCapacitatedCode());
        return model;
    }
}