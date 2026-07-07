package com.transit.tickets.dto.request;

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
public class TicketRequestDTO {

    @NotBlank(message = "Ticket code cannot be blank")
    @Size(min = 3, max = 20, message = "Code must be between 3 and 20 characters")
    private String code;

    @NotBlank(message = "Origin city code cannot be blank")
    @Size(min = 3, max = 10, message = "Origin city code must be between 3 and 10 characters")
    private String cityCodeOrigin;

    @NotBlank(message = "Destination city code cannot be blank")
    @Size(min = 3, max = 10, message = "Destination city code must be between 3 and 10 characters")
    private String cityCodeDestination;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be a positive value")
    private Double price;

    @NotNull(message = "Client ID cannot be null")
    private Long clientId;

    @NotNull(message = "Train ID cannot be null")
    private Long trainId;

    @NotNull(message = "Departure date cannot be null")
    private Date departureDate;
}