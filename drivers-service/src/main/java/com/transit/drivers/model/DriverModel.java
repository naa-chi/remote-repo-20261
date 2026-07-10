package com.transit.drivers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "drivers", schema = "transport_db_driversservice")
public class DriverModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "driver code cannot be blank")
    @Size(min = 8, max = 10, message = "Code must be between 8 and 10 characters")
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull
    @Column(nullable = false)
    @Min(value = 1)
    private Long salary;

    @NotNull
    @Column(name = "contract_date", nullable = false)
    private Date contractDate;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private Date dateOfBirth;

    @NotBlank
    @Size(min = 1, max = 120)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 120)
    @Column(name = "second_name", nullable = false)
    private String secondName;

    @NotBlank
    @Size(min = 1, max = 1)
    @Column(name = "trained_on_type_trains", nullable = false)
    private String capacitatedCode; // A, B, C, etc.
}