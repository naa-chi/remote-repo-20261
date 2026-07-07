package com.transit.trains.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "trains", schema = "transport_db_trainsservice")
public class TrainModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Code cannot be null")
    @NotBlank(message = "Code cannot be blank")
    @Column(unique = true, length = 5, nullable = false)
    private String code;

    @NotNull(message = "Manufacturer has to be specified")
    @NotBlank(message = "Manufacturer cannot be blank")
    @Column(name = "manufacturer_id", length = 100, nullable = false)
    private String manufacturerId;

    @NotNull(message = "Engine id cannot be null")
    // FIXED: Removed @NotBlank because Long cannot be blank
    @Column(name = "engine_id", nullable = false)
    private Long engineId;

    @NotNull(message = "Car amount can't be null")
    @Min(value = 1, message = "Car amount must be a positive integer")
    @Column(name = "car_amount", nullable = false)
    private Integer carAmount;

    @NotNull(message = "We need to know the costs")
    @Min(value = 0, message = "Costs must be a positive integer")
    @Column(name = "costs", nullable = false)
    private Integer costPerCar;

    @NotNull(message = "We need to know the date of production")
    @Column(name = "production_date", nullable = false)
    private Date productionDate;
}