package com.gTransitProject.driver.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class driverModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

    @NotBlank
    @Column(unique = false, length = 100, nullable = false)
    private String driverName;

    @NotBlank
    @Column(unique = true, length = 20, nullable = false)
    private String driverLicenseNumber;

    @NotBlank
    @Column(length = 20, nullable = false)
    private String driverContactNumber;

    @NotBlank
    @Column(unique = false, nullable = false)
    private Integer monthlyWage;

    @NotBlank
    @Column(unique = false, nullable = false)
    private Integer expenses;
}
