package com.transit.reviews.dto.response;

import org.springframework.hateoas.RepresentationModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TicketResponseDTO extends RepresentationModel<TicketResponseDTO> {
    private Long id;
    private String code;
    private String cityCodeOrigin;
    private String cityCodeDestination;
    private Double price;
    private Long clientId;
    private Long trainId;
    private Date departureDate;
}