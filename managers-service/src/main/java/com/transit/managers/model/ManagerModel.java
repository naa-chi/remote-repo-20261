package com.transit.managers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@SuppressWarnings("all")

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "managers", schema = "transport_db_managersservice")
public class ManagerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Manager code cannot be blank")
    @Size(min = 7, max = 13, message = "Code must be between 7 and 13 characters")
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull
    @Column(nullable = false)
    private Long salary;

    @NotNull
    @Column(name = "contract_date", nullable = false)
    private Date contractDate;

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
    @Column(name = "manager_group", nullable = false)   // renamed to avoid SQL keyword
    private String managerGroup; // A, B, C, etc.
}