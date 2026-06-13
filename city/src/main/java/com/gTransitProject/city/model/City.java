package com.gTransitProject.city.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

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

    @NotBlank
    @Column(length = 60, nullable = false)
    private String cityName;

    @NotBlank
    @Column(unique = true, length = 4, nullable = false)
    private String cityCode;

    @Column(nullable = false)
    private Integer lineNumber;

    @Min(value = 1)
    @Column(name = "inhabitants", nullable = false)
    private Integer population;
}
