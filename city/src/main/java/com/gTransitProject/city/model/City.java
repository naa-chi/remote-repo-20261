package com.gTransitProject.city.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cityId;

    @NotNull(message = "Can't be null")
    @Column(length = 60, nullable = false)
    private String cityName;

    @NotNull(message = "Can't be null")
    @Column(unique = true, length = 4, nullable = false)
    private String cityCode;

    @NotNull(message = "Can't be null")
    @Column(nullable = false)
    private Integer lineNumber;

    @Min(value = 1, message = "A city must have at least 1 inhabitant")
    @Max(value = 10000000, message = "A city cannot exceed 10 million inhabitants")
    @NotNull(message = "Why would there be a place with 0 people?")
    @Column(name = "inhabitants", nullable = false)
    private Integer population;
}