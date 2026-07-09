package com.transit.lines.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineResponseDTO extends RepresentationModel<LineResponseDTO> {
    private Long id;
    private Integer lineCode;
    private Long lineLengthKM;
    private Long peopleServedMonthlyEstimate;
}