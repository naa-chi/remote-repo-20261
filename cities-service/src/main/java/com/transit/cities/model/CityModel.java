package com.transit.cities.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
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
@Table(name = "cities", schema = "transport_db_citiesservice")
public class CityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "city_code", length = 3, nullable = false, unique = true)
    private String threeLetterCityCode;

    @NotNull
    @NotBlank
    @Column(name = "city_name", length = 256, nullable = false, unique = false) //made unnecessarily large in case Bangkok's full name has to be used for some reason
    private String fullCityName;

    @NotNull(message = "date cannot be null")
    @Column(name = "founding_date", nullable = false)
    private Date foundingCityDate;

    @NotNull
    @Column(name = "city_population", nullable = false)
    @Min(value=20) @Max(value=860000000)
    private Long cityPopulation;

    @NotNull
    @NotBlank
    @Column(name = "country_code", length = 2, nullable = false, unique = false)
    private String countryCode; //US, CA, DE, RU, ZH, VN, KR, IN, etc
}
