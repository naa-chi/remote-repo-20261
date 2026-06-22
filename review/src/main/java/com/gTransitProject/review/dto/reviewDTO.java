package com.gTransitProject.review.dto;

//dto or something idk man i'm lost

import lombok.Data;

@Data
public class reviewDTO {
    private Integer clientId;
    private Integer rating;
    private String comment;
    private String route;
    private String reviewDate;
    private Integer specificTrainId;
    //Makes sense to expose these fields
}
