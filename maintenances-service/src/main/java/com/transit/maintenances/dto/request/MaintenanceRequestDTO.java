package com.transit.maintenances.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestDTO {

    @NotNull(message = "Description can't be null")
    @NotBlank(message = "Description can't be blank")
    private String maintenanceDescription;

    @NotNull(message = "Entry date cannot be null")
    private Date maintenanceEntryDate;

    @NotNull(message = "End date cannot be null")
    private Date maintenanceEndDate;

    @NotNull(message = "Release date cannot be null")
    private Date maintenanceReleaseDate;

    @NotNull(message = "Crew group cannot be null")
    @NotBlank(message = "Crew group cannot be blank")
    private String maintenanceCrewGroup;

    @NotNull(message = "Cost cannot be null")
    @Min(value = 1, message = "Cost must be at least 1")
    @Max(value = 2147483647, message = "Cost exceeds maximum allowed value")
    private Integer maintenancePrice;

    @NotNull(message = "Engine code has to be specified")
    @NotBlank(message = "Engine code cannot be blank")
    private String engineCode;

    @NotNull(message = "Train ID cannot be null")
    private Long trainId;

    @NotNull(message = "Maintenance ID cannot be null")
    @NotBlank(message = "Maintenance ID cannot be blank")
    private String maintenanceId;
}