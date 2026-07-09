package com.transit.lines.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineRequestDTO {
    @NotNull
    private Integer lineCode;

    @NotNull
    @Min(0) @Max(999999999)
    private Long lineLengthKM;

    @NotNull
    private Long peopleServedMonthlyEstimate;
}