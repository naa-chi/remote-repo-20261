package com.gTransitProject.train.model;

import java.sql.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "train")
@NoArgsConstructor
@AllArgsConstructor
public class train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer specificTrainId;

    @NotNull(message = "Code cannot be null. We need to know the unique identifier for this train.")
    @Column(unique = true, length = 5, nullable = false)
    private String code;

    // These MUST be named exactly like this for the Service to work
    @NotNull(message = "Manufacturer ID cannot be null. We need to know who made this train.")
    @Column(name = "manufacturer_id")
    private Integer manufacturerId;

    @NotNull(message = "Engine Type ID cannot be null. We need to know what type of engine this train has.")
    @Column(name = "engine_type_id")
    private Integer engineTypeId;

    @NotNull(message = "Car Amount cannot be null. We need to know how many cars this train has.")
    @Column(nullable = false)
    @Min(value = 1)
    private Integer carAmount;

    @NotNull(message = "Manufacturing Date cannot be null. We need to know when this train was made.")
    @Column(nullable = false)
    private Date manufacturingDate;

    @NotNull(message = "Price Per Car cannot be null. We need to know the cost of each car.")
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