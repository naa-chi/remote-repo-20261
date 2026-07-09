package com.transit.manufacturers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "manufacturers", schema = "transport_db_manufacturersservice")
public class ManufacturerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "manufacturerId cannot be null")
    @NotBlank(message = "manufacturerId cannot be blank")
    @Column(name = "manufacturer_id", length = 4, nullable = false, unique = true)
    private String manufacturerId;   // renamed from manufacturerid

    @NotNull(message = "country cannot be null")
    @NotBlank(message = "country cannot be blank")
    @Column(name = "country", length = 2, nullable = false)
    private String countryOfOrigin;

    @NotNull(message = "founding date cannot be null")
    @Column(name = "founding_date", nullable = false)
    private Date foundingDate;

    @NotNull(message = "revenue cannot be null")
    @Column(name = "profit", nullable = false)
    private Long revenue;
}