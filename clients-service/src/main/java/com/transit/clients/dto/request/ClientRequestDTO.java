package com.transit.clients.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDTO {
    @NotBlank
    @Size(min = 3, max = 20)
    private String code;

    @NotBlank
    @Size(min = 1, max = 120)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 120)
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 10, max = 20)
    private String phoneNumber;

    @NotNull
    private Date registrationDate;
}