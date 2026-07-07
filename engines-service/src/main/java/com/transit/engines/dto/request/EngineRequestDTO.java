package com.transit.engines.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineRequestDTO {

    @NotNull(message = "Engine ID cannot be null")
    private Long engineId;

    @NotNull(message = "Manufacturer has to be specified")
    @NotBlank(message = "Manufacturer cannot be blank")
    private String manufacturerId;

    @NotNull(message = "Engine code has to be specified")
    @NotBlank(message = "Engine cannot be blank")
    private String engineCode;

    @NotNull(message = "HP count cannot be null")
    @Min(value = 0, message = "HP count has to be a positive number")
    private Float engineHorsepower;

    @NotNull(message = "Weight count cannot be null")
    @Min(value = 0, message = "Weight has to be a positive number")
    private Float engineWeight;

    @NotNull(message = "Price count cannot be null")
    @Min(value = 0, message = "Price count has to be a positive number")
    private Float enginePrice;

    @NotNull(message = "We need to know the date of production")
    private Date productionDate;
}