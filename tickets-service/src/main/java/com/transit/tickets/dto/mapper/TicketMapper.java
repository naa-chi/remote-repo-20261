package com.transit.tickets.dto.mapper;

import com.transit.tickets.dto.request.TicketRequestDTO;
import com.transit.tickets.dto.response.TicketResponseDTO;
import com.transit.tickets.model.TicketModel;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponseDTO toResponse(TicketModel model) {
        if (model == null) return null;

        TicketResponseDTO response = new TicketResponseDTO();
        response.setId(model.getId());
        response.setCode(model.getCode());
        response.setCityCodeOrigin(model.getCityCodeOrigin());
        response.setCityCodeDestination(model.getCityCodeDestination());
        response.setPrice(model.getPrice());
        response.setClientId(model.getClientId());
        response.setTrainId(model.getTrainId());
        response.setDepartureDate(model.getDepartureDate());

        return response;
    }

    public TicketModel toEntity(TicketRequestDTO request) {
        if (request == null) return null;

        TicketModel model = new TicketModel();
        model.setCode(request.getCode());
        model.setCityCodeOrigin(request.getCityCodeOrigin());
        model.setCityCodeDestination(request.getCityCodeDestination());
        model.setPrice(request.getPrice());
        model.setClientId(request.getClientId());
        model.setTrainId(request.getTrainId());
        model.setDepartureDate(request.getDepartureDate());

        return model;
    }
}