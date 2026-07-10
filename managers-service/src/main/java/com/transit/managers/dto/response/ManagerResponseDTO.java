package com.transit.managers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ManagerResponseDTO extends RepresentationModel<ManagerResponseDTO> {
    private Long id;
    private String code;
    private Long salary;
    private Date contractDate;
    private String firstName;
    private String secondName;
    private String managerGroup;
}