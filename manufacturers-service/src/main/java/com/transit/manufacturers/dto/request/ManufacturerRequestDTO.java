package com.transit.manufacturers.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerRequestDTO {

    @NotNull(message = "Manufacturer ID cannot be null")
    @NotBlank(message = "Manufacturer ID cannot be blank")
    private String manufacturerId;

    @NotNull(message = "Country cannot be null")
    @NotBlank(message = "Country cannot be blank")
    private String countryOfOrigin;

    @NotNull(message = "Founding date cannot be null")
    private Date foundingDate;

    @NotNull(message = "Revenue cannot be null")
    private Long revenue;
}