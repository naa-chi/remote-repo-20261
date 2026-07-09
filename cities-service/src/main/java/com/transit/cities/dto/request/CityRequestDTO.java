package com.transit.cities.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityRequestDTO {
    @NotBlank private String threeLetterCityCode;
    @NotBlank private String fullCityName;
    @NotNull private Date foundingCityDate;
    @NotNull private Long cityPopulation;
    @NotBlank private String countryCode;
}