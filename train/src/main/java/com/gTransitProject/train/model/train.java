package com.gTransitProject.train.model;

import java.sql.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "train")
@NoArgsConstructor
@AllArgsConstructor
public class train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer specificTrainId;

    @Column(unique = true, length = 5, nullable = false)
    private String code;

    // These MUST be named exactly like this for the Service to work
    @Column(name = "manufacturer_id")
    private Integer manufacturerId;

    @Column(name = "engine_type_id")
    private Integer engineTypeId;

    @Column(nullable = false)
    @Min(value = 1)
    private Integer carAmount;

    @Column(nullable = false)
    private Date manufacturingDate;

    @Column(nullable = false)
    private Integer pricePerCar;

    @ManyToOne 
    @JoinColumn(name = "type_code_id", referencedColumnName = "id") 
    private typeTrain typeTrain;

    public Integer getSpecificTrainId() {
        return specificTrainId;
    }

    public void setSpecificTrainId(Integer specificTrainId) {
        this.specificTrainId = specificTrainId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getEngineTypeId() {
        return engineTypeId;
    }

    public void setEngineTypeId(Integer engineTypeId) {
        this.engineTypeId = engineTypeId;
    }

    public Integer getCarAmount() {
        return carAmount;
    }

    public void setCarAmount(Integer carAmount) {
        this.carAmount = carAmount;
    }

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Date manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Integer getPricePerCar() {
        return pricePerCar;
    }

    public void setPricePerCar(Integer pricePerCar) {
        this.pricePerCar = pricePerCar;
    } 

    
}