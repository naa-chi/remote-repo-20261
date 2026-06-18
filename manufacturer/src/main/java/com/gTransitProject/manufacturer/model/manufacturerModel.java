package com.gTransitProject.manufacturer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
// Specifying the schema for the manufacturer table
@Table(name = "manufacturer", schema = "master_data_schema") 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class manufacturerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Manufacturer Name cannot be null. We need to know the name of this manufacturer.")
    private String name;

    @Column(name = "country_of_origin")
    @NotNull(message = "Country of Origin cannot be null. We need to know where this manufacturer is from.")
    private String country;
}