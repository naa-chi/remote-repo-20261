package com.gTransitProject.train.dto;
import com.gTransitProject.train.model.typeTrain;
import lombok.Data;

@Data
public class trainDTO {
    private Integer specificTrainId;
    private String code;
    private typeTrain typeTrain;
}
