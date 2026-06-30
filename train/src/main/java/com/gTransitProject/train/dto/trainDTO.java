package com.gTransitProject.train.dto;
import com.gTransitProject.train.model.typeTrain;
import lombok.Data;
import java.sql.Date;

@Data
public class trainDTO {
    private Integer specificTrainId;
    private String code;
    private typeTrain typeTrain;
    private Integer carAmount;
    private Date manufacturingDate;
    private Integer pricePerCar;
}
