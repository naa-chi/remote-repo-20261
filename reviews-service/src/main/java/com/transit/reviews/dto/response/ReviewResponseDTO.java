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
public class ReviewResponseDTO extends RepresentationModel<ReviewResponseDTO> {
    private Long id;
    private Long clientId;
    private Long trainId;
    private Integer rating;
    private String comment;
    private Date reviewDate;
}