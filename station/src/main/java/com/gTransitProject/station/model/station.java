package com.gTransitProject.station.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "station")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stationId;

    @Column(unique = true, length = 6, nullable = false)
    @NotNull(message = "Unique Station Code cannot be null. We need to know the unique identifier for this station.")
    private String uniqueStationCode;

    @Column(length = 50, nullable = false)
    @NotNull(message = "Station Name cannot be null. We need to know the name of this station.")
    private String stationName;

    @Column(nullable = false)
    @NotNull(message = "City Code cannot be null. We need to know the city where this station is located.")
    private String cityCode;

    @Column(nullable = false)
    @NotNull(message = "Line Number cannot be null. We need to know which line this station belongs to.")
    private Integer lineNumber;
}