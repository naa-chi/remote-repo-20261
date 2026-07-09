package com.transit.maintenances.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "maintenance_reports", schema="transport_db_maintenancesservice")
public class MaintenanceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Description can't be null")
    @NotBlank(message = "Description can't be blank")
    @Column(name = "maintenance_description", length=500, nullable = false)
    private String maintenaceDescription;

    @NotNull(message = "Date cannot be null")
    @Column(name = "maintenance_entry_date", nullable = false)
    private Date maintenanceEntryDate; //date where the vehicle enters maintenance

    @NotNull(message = "Date cannot be null")
    @Column(name = "maintenance_end_date", nullable = false)
    private Date maintenanceEndDate; //date where maintenance is finished

    @NotNull(message = "Date cannot be null")
    @Column(name = "maintenance_release_date", nullable = false)
    private Date maintenanceReleaseDate; //date where the vehicle is fully released and back in operation

    @NotNull(message = "Crew group cannot be null")
    @Column(name = "maintenance_crew", length = 15, nullable = false)
    private String maintenanceCrewGroup;

    @NotNull(message = "cost cannot be null")
    @Min(value = 1, message = "price cannot be one")
    @Max(value = 2147483647)
    private Integer maintenancePrice; //we gotta pay the maintenance workers

    @NotNull(message = "engine code has to be specified")
    @NotBlank(message = "engine cannot be blank")
    @Column(name = "engine_code", length = 100, nullable = false)
    private String engineCode; //fk, missing dto for the time being

    @NotNull(message = "engine code has to be specified")
    @Column(name = "train_id", nullable = false)
    private Long trainId; //fk, from train (uses numeric id because there is no dedicated id field for it)

    @NotNull(message = "maintenance id cannot be null")
    @NotBlank(message = "maintenance id cannot be blank")
    @Column(name = "maintenance_id", length = 20, nullable = false, unique = true)
    private String maintenanceId;
}
