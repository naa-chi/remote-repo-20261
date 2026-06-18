package com.gTransitProject.auth.dto;

import lombok.Data;

@Data
public class AuthDTO {

    private Integer authId;

    private String requestDescription;

    private String originCity;

    private String destinationCity;

    private String authCode;

    private String supervisorCode;

    private Boolean authorized;
}