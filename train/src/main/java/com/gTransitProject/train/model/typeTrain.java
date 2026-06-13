package com.gTransitProject.train.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "train_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class typeTrain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 20, nullable = false)
    private String type; //cargo, passenger
    
    @Column(unique = true, nullable = false)
    @Min(value = 10)
    @Max(value = 20)
    private Integer typeCode; //10, 20 as seen above
}
