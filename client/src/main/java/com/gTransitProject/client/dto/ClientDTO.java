package com.gTransitProject.client.dto;

import lombok.Data;

@Data
public class ClientDTO {

    private Integer clientId;

    private String clientName;

    private String requestCity;

    private String providerCity;

    private String requestedResource;

    private String offeredReward;

    private String authCode;

    private Boolean active;
}