package com.transit.trains.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineResponseDTO {
    private Long id;
    private Long engineId;
    private String manufacturerId;
    private String engineCode;
    private Float engineHorsepower;
    private Float engineWeight;
    private Float enginePrice;
    private Date productionDate;
}