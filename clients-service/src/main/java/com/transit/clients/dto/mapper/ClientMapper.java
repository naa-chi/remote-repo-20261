package com.transit.clients.dto.mapper;

import com.transit.clients.dto.request.ClientRequestDTO;
import com.transit.clients.dto.response.ClientResponseDTO;
import com.transit.clients.model.ClientModel;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResponseDTO toResponse(ClientModel model) {
        if (model == null) return null;
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(model.getId());
        dto.setCode(model.getCode());
        dto.setFirstName(model.getFirstName());
        dto.setLastName(model.getLastName());
        dto.setEmail(model.getEmail());
        dto.setPhoneNumber(model.getPhoneNumber());
        dto.setRegistrationDate(model.getRegistrationDate());
        return dto;
    }

    public ClientModel toEntity(ClientRequestDTO dto) {
        if (dto == null) return null;
        ClientModel model = new ClientModel();
        model.setCode(dto.getCode());
        model.setFirstName(dto.getFirstName());
        model.setLastName(dto.getLastName());
        model.setEmail(dto.getEmail());
        model.setPhoneNumber(dto.getPhoneNumber());
        model.setRegistrationDate(dto.getRegistrationDate());
        return model;
    }
}