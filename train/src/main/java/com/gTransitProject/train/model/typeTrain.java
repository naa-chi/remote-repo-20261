package com.gTransitProject.train.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Entity
@Table(name = "train_type")
@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class typeTrain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Type cannot be null. We need to know what type of train this is.")
    @Column(unique = true, length = 20, nullable = false)
    private String type; //cargo, passenger
    
    @NotNull(message = "Type Code cannot be null. We need to know the unique identifier for this train type.")
    @Column(unique = true, nullable = false)
    @Min(value = 10)
    @Max(value = 20)
    private Integer typeCode; //10, 20 as seen above
}
