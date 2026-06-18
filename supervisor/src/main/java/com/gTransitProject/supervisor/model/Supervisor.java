package com.gTransitProject.supervisor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name="supervisors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supervisorId;

    @NotBlank
    private String supervisorName;

    @NotBlank
    @Column(unique = true)
    private String supervisorCode;

    @NotBlank
    private String cityCode;

    private Boolean authorized;
}