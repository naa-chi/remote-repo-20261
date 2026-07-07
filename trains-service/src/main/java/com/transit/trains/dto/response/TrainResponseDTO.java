package com.transit.trains.dto.response;

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
public class TrainResponseDTO extends RepresentationModel<TrainResponseDTO> {
    private Long id;
    private String code;
    private String manufacturerId;
    private Long engineId;
    private Integer carAmount;
    private Integer costPerCar;
    private Date productionDate;
}