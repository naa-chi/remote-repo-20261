package com.gTransitProject.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "auths")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authId;

    @NotBlank
    private String requestDescription;

    @NotBlank
    private String originCity;

    @NotBlank
    private String destinationCity;

    @NotBlank
    @Column(unique = true)
    private String authCode;

    @NotBlank
    private String supervisorCode;

    private Boolean authorized;
}