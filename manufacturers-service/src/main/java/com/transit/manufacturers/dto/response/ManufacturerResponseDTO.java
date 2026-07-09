package com.transit.manufacturers.dto.response;

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
public class ManufacturerResponseDTO extends RepresentationModel<ManufacturerResponseDTO> {

    private Long id;
    private String manufacturerId;
    private String countryOfOrigin;
    private Date foundingDate;
    private Long revenue;
}