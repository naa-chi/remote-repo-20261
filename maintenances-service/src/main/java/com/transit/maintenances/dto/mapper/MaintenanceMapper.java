package com.transit.maintenances.dto.mapper;

import com.transit.maintenances.dto.request.MaintenanceRequestDTO;
import com.transit.maintenances.dto.response.MaintenanceResponseDTO;
import com.transit.maintenances.model.MaintenanceModel;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper {

    public MaintenanceResponseDTO toResponse(MaintenanceModel model) {
        if (model == null) {
            return null;
        }

        MaintenanceResponseDTO response = new MaintenanceResponseDTO();
        response.setId(model.getId());
        response.setMaintenanceDescription(model.getMaintenaceDescription());
        response.setMaintenanceEntryDate(model.getMaintenanceEntryDate());
        response.setMaintenanceEndDate(model.getMaintenanceEndDate());
        response.setMaintenanceReleaseDate(model.getMaintenanceReleaseDate());
        response.setMaintenanceCrewGroup(model.getMaintenanceCrewGroup());
        response.setMaintenancePrice(model.getMaintenancePrice());
        response.setEngineCode(model.getEngineCode());
        response.setTrainId(model.getTrainId());
        response.setMaintenanceId(model.getMaintenanceId());

        return response;
    }

    public MaintenanceModel toEntity(MaintenanceRequestDTO request) {
        if (request == null) {
            return null;
        }

        MaintenanceModel model = new MaintenanceModel();
        model.setMaintenaceDescription(request.getMaintenanceDescription());
        model.setMaintenanceEntryDate(request.getMaintenanceEntryDate());
        model.setMaintenanceEndDate(request.getMaintenanceEndDate());
        model.setMaintenanceReleaseDate(request.getMaintenanceReleaseDate());
        model.setMaintenanceCrewGroup(request.getMaintenanceCrewGroup());
        model.setMaintenancePrice(request.getMaintenancePrice());
        model.setEngineCode(request.getEngineCode());
        model.setTrainId(request.getTrainId());
        model.setMaintenanceId(request.getMaintenanceId());

        return model;
    }
}