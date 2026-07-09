package com.transit.stations.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "stations", schema = "transport_db_stationsservice")
public class StationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "station_code", length=5, unique = true, nullable = false)
    private String stationCode;

    @NotNull
    @NotBlank
    @Column(name = "city_code", length=5, unique = false, nullable = true)
    private String cityCode;  //grab via feignclient on cities-service

    @NotNull
    @Column(name = "line-1", unique = false, nullable = false) //Primary line
    private Integer lineCode1;

    @Column(name = "line-2", unique = false, nullable = true) //Secondary line
    private Integer lineCode2;
    
    @Column(name = "line-3", unique = false, nullable = true) //Triary line
    private Integer lineCode3;

    @Column(name = "line-4", unique = false, nullable = true) //Quarterary line
    private Integer lineCode4;
    
    //Grab all of the above from lines via feignclient and dto and all that implies
}
