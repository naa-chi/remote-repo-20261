package com.gTransitProject.client.model;

import java.sql.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

    @Column(unique = false, length=50, nullable=false) // Juan 
    private String firstName;

    @Column(unique = false, length=50, nullable=true) // Jose 
    private String lastName;

    @Column(unique = false, length=50, nullable=false) // del Campo 
    private String paternalName;

    @Column(unique = false, length=50, nullable=true) // de la Cruz (ej)
    private String maternalName;

    @Column(unique = false, nullable = false)
    private Date birthDate;
}
