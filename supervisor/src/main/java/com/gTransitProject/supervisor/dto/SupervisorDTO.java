package com.gTransitProject.supervisor.dto;

import lombok.Data;

@Data
public class SupervisorDTO {

    private Integer supervisorId;

    private String supervisorName;

    private String supervisorCode;

    private String cityCode;

    private Boolean authorized;
}