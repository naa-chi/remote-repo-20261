package com.transit.engines.dto.response;

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
public class EngineResponseDTO extends RepresentationModel<EngineResponseDTO> {
    private Long id;
    private Long engineId;
    private String manufacturerId;
    private String engineCode;
    private Float engineHorsepower;
    private Float engineWeight;
    private Float enginePrice;
    private Date productionDate;
}