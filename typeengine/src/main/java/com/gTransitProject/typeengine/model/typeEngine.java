package com.gTransitProject.typeengine.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
// Specifying the schema for the engine types
@Table(name = "engine_type")
@NoArgsConstructor
@AllArgsConstructor
public class typeEngine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Type cannot be null.")
    @Column(nullable = false, length = 20)
    private String type; 

    @NotNull(message = "We need to know how powerful this engine is.")
    @Min(value = 1)
    private float horsepower;
    
    @Column(unique = true, nullable = false)
    @NotNull(message = "Type code cannot be null.")
    private Integer typeCodeEngine;

    @Column(unique = true, length = 40, nullable = false)
    @NotNull(message = "We need to know the unique name for this engine type.")
    private String nameCodeEngine;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(float horsepower) {
        this.horsepower = horsepower;
    }

    public Integer getTypeCodeEngine() {
        return typeCodeEngine;
    }

    public void setTypeCodeEngine(Integer typeCodeEngine) {
        this.typeCodeEngine = typeCodeEngine;
    }

    public String getNameCodeEngine() {
        return nameCodeEngine;
    }

    public void setNameCodeEngine(String nameCodeEngine) {
        this.nameCodeEngine = nameCodeEngine;
    }


}
