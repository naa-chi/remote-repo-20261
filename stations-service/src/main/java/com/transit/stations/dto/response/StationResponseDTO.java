package com.transit.stations.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StationResponseDTO extends RepresentationModel<StationResponseDTO> {
    private Long id;
    private String stationCode;
    private String cityCode;

    // Enriched from Cities service
    private String cityName;

    private Integer lineCode1;
    private Long line1Length;          // enriched from Lines
    private Long line1PeopleServed;    // enriched

    private Integer lineCode2;
    private Long line2Length;
    private Long line2PeopleServed;

    private Integer lineCode3;
    private Long line3Length;
    private Long line3PeopleServed;

    private Integer lineCode4;
    private Long line4Length;
    private Long line4PeopleServed;
}