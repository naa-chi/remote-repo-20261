package com.transit.maintenances.dto.response;

import org.springframework.hateoas.RepresentationModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MaintenanceResponseDTO extends RepresentationModel<MaintenanceResponseDTO> {
    private Long id;
    private String maintenanceDescription;
    private Date maintenanceEntryDate;
    private Date maintenanceEndDate;
    private Date maintenanceReleaseDate;
    private String maintenanceCrewGroup;
    private Integer maintenancePrice;
    private String engineCode;
    private Long trainId;
    private String maintenanceId;
}