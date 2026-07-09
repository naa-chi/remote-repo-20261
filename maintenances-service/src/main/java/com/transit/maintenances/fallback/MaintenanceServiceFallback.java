package com.transit.maintenances.fallback;

import com.transit.maintenances.dto.response.MaintenanceResponseDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class MaintenanceServiceFallback {

    public MaintenanceResponseDTO getMaintenanceFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for Maintenance ID " + id + ". Error: " + t.getMessage());
        return createDefaultFallbackMaintenance(id != null ? id : -1L);
    }

    public MaintenanceResponseDTO getMaintenanceFallback(String maintenanceId, Throwable t) {
        System.err.println("CircuitBreaker triggered for Maintenance code " + maintenanceId + ". Error: " + t.getMessage());
        return createDefaultFallbackMaintenance(-1L);
    }

    public List<MaintenanceResponseDTO> getMaintenancesFallbackList(Throwable t) {
        System.err.println("CircuitBreaker triggered for maintenances list retrieval. Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackMaintenance(-1L));
    }

    public List<MaintenanceResponseDTO> getMaintenancesFallbackList(String param, Throwable t) {
        System.err.println("CircuitBreaker triggered for maintenances list by parameter. Error: " + t.getMessage());
        return Collections.singletonList(createDefaultFallbackMaintenance(-1L));
    }

    private MaintenanceResponseDTO createDefaultFallbackMaintenance(Long id) {
        MaintenanceResponseDTO fallbackDto = new MaintenanceResponseDTO();
        fallbackDto.setId(id);
        fallbackDto.setMaintenanceDescription("ERROR");
        fallbackDto.setMaintenanceEntryDate(Date.valueOf(LocalDate.now()));
        fallbackDto.setMaintenanceEndDate(Date.valueOf(LocalDate.now()));
        fallbackDto.setMaintenanceReleaseDate(Date.valueOf(LocalDate.now()));
        fallbackDto.setMaintenanceCrewGroup("ERROR");
        fallbackDto.setMaintenancePrice(0);
        fallbackDto.setEngineCode("ERROR");
        fallbackDto.setTrainId(0L);
        fallbackDto.setMaintenanceId("ERROR");
        return fallbackDto;
    }
}