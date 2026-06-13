package com.gTransitProject.train.dto;

import lombok.Data;

@Data
public class trainDTO {
    private Integer specificTrainId;
    private String code;
    private typeTrain typeTrain;
}
