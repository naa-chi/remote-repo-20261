package com.transit.engines.dto;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EngineDTO extends RepresentationModel<EngineDTO>{
    private Long id;

    @NotNull(message = "Manufacturer has to be specified")
    @NotBlank(message = "Manufacturer cannot be blank")
    @Column(name = "manufacturer_id", length = 100, nullable = false)
    private String manufacturerId;

    @NotNull(message = "engine code has to be specified")
    @NotBlank(message = "engine cannot be blank")
    private String engineCode;

    @NotNull(message = "hp count cannot be null")
    @Min(value = 0, message = "hp count has to be a positive number")
    private Float engineHorsepower;

    @NotNull(message = "weight count cannot be null")
    @Min(value = 0, message = "weight has to be a positive number")
    private Float engineWeight;

    @NotNull(message = "price count cannot be null")
    @Min(value = 0, message = "price count has to be a positive number")
    private Float enginePrice;

    @NotNull(message = "We need to know the date of production")
    private Date productionDate; 

}
