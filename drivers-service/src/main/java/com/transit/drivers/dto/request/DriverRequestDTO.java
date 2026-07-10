package com.transit.drivers.dto.request;

import jakarta.validation.constraints.Min;
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
public class DriverRequestDTO {
    @NotBlank
    @Size(min = 8, max = 10)
    private String code;

    @NotNull
    @Min(1)
    private Long salary;

    @NotNull
    private Date contractDate;

    @NotNull
    private Date dateOfBirth;

    @NotBlank
    @Size(min = 1, max = 120)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 120)
    private String secondName;

    @NotBlank
    @Size(min = 1, max = 1)
    private String capacitatedCode;
}