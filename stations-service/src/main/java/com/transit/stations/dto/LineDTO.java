package com.transit.stations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineDTO {
    private Integer lineCode;
    private Long lineLengthKM;
    private Long peopleServedMonthlyEstimate;
}