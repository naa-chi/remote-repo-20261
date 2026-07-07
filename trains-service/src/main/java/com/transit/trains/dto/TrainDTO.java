package com.transit.trains.dto;

import org.springframework.hateoas.RepresentationModel;
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
@EqualsAndHashCode(callSuper = false) // Crucial: tells Lombok to respect the parent class (RepresentationModel)
public class TrainDTO extends RepresentationModel<TrainDTO> {
    
    private Long id;

    @NotNull(message = "Code cannot be null")
    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotNull(message = "Manufacturer has to be specified")
    @NotBlank(message = "Manufacturer cannot be blank")
    private String manufacturerId;

    @NotNull(message = "Engine id cannot be null")
    private Long engineId;

    @NotNull(message = "Car amount can't be null")
    @Min(value = 1, message = "Car amount must be a positive integer")
    private Integer carAmount;

    @NotNull(message = "We need to know the costs")
    @Min(value = 0, message = "Costs must be a positive integer")
    private Integer costPerCar;

    @NotNull(message = "We need to know the date of production")
    private Date productionDate;
}