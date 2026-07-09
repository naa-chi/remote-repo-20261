package com.transit.cities.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CityResponseDTO extends RepresentationModel<CityResponseDTO> {
    private Long id;
    private String threeLetterCityCode;
    private String fullCityName;
    private Date foundingCityDate;
    private Long cityPopulation;
    private String countryCode;
}