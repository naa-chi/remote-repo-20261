package com.gTransitProject.manufacturer.model;

import jakarta.persistence.*;
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
    private String name;

    @Column(name = "country_of_origin")
    private String country;
}