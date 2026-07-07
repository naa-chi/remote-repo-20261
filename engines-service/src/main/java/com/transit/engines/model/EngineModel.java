package com.transit.engines.model;

import java.sql.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "engines", schema = "transport_db_enginesservice")
public class EngineModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Engine id cannot be null")
    @Column(name = "engine_id", nullable = false)
    private Long engineId;

    @NotNull(message = "Manufacturer has to be specified")
    @NotBlank(message = "Manufacturer cannot be blank")
    @Column(name = "manufacturer_id", length = 100, nullable = false)
    private String manufacturerId;

    @NotNull(message = "engine code has to be specified")
    @NotBlank(message = "engine cannot be blank")
    @Column(name = "engine_code", length = 100, nullable = false)
    private String engineCode;

    @NotNull(message = "hp count cannot be null")
    @Min(value = 0, message = "hp count has to be a positive number")
    private Float engineHorsepower;

    @NotNull(message = "weight count cannot be null")
    @Min(value = 0, message = "weight count has to be a positive number")
    private Float engineWeight;

    @NotNull(message = "price count cannot be null")
    @Min(value = 0, message = "price count has to be a positive number")
    private Float enginePrice;

    @NotNull(message = "We need to know the date of production")
    @Column(name = "production_date", nullable = false)
    private Date productionDate; 
}
