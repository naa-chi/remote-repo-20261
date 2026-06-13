package com.gTransitProject.station.model;

import jakarta.persistence.*;
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
    private String uniqueStationCode;

    @Column(length = 50, nullable = false)
    private String stationName;

    @Column(nullable = false)
    private String cityCode;

    @Column(nullable = false)
    private Integer lineNumber;
}