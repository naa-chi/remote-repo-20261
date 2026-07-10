package com.transit.managers.dto.mapper;

import com.transit.managers.dto.request.ManagerRequestDTO;
import com.transit.managers.dto.response.ManagerResponseDTO;
import com.transit.managers.model.ManagerModel;
import org.springframework.stereotype.Component;

@Component
public class ManagerMapper {

    public ManagerResponseDTO toResponse(ManagerModel model) {
        if (model == null) return null;
        ManagerResponseDTO dto = new ManagerResponseDTO();
        dto.setId(model.getId());
        dto.setCode(model.getCode());
        dto.setSalary(model.getSalary());
        dto.setContractDate(model.getContractDate());
        dto.setFirstName(model.getFirstName());
        dto.setSecondName(model.getSecondName());
        dto.setManagerGroup(model.getManagerGroup());
        return dto;
    }

    public ManagerModel toEntity(ManagerRequestDTO dto) {
        if (dto == null) return null;
        ManagerModel model = new ManagerModel();
        model.setCode(dto.getCode());
        model.setSalary(dto.getSalary());
        model.setContractDate(dto.getContractDate());
        model.setFirstName(dto.getFirstName());
        model.setSecondName(dto.getSecondName());
        model.setManagerGroup(dto.getManagerGroup());
        return model;
    }
}