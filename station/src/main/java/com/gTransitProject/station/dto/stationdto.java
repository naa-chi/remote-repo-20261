package com.gTransitProject.station.dto;
import lombok.Data;

@Data
public class stationdto {
    private String uniqueStationCode;
    private String stationName;
    private String cityCode;
    private Integer lineNumber;
}
