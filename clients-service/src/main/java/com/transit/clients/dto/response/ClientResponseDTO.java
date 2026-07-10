package com.transit.clients.dto.response;

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
public class ClientResponseDTO extends RepresentationModel<ClientResponseDTO> {
    private Long id;
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date registrationDate;
}