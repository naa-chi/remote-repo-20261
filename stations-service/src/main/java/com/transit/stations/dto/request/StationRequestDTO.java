package com.transit.stations.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationRequestDTO {
    @NotBlank
    private String stationCode;

    @NotBlank
    private String cityCode;

    @NotNull
    private Integer lineCode1;

    private Integer lineCode2;
    private Integer lineCode3;
    private Integer lineCode4;
}