package com.gTransitProject.client.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientId;

    @NotBlank
    private String clientName;

    @NotBlank
    private String requestCity;

    @NotBlank
    private String providerCity;

    @NotBlank
    private String requestedResource;

    @NotBlank
    private String offeredReward;

    @NotBlank
    @Column(unique = true)
    private String authCode;

    private String status;

    public String getStatus() {
        return status;
    }
}