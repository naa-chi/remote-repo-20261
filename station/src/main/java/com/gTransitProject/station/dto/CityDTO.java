package com.gTransitProject.station.dto;

import lombok.Data;

@Data
public class CityDTO {

    private String cityName;
    private String cityCode;
    private Integer lineNumber;
    private Integer population;
}