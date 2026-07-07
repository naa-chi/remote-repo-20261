package com.transit.tickets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tickets", schema = "transport_db_ticketsservice")
public class TicketModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ticket code cannot be blank")
    @Size(min = 3, max = 20, message = "Code must be between 3 and 20 characters")
    @Column(unique = true, nullable = false)
    private String code;

    @NotBlank(message = "Origin city code cannot be blank")
    @Size(min = 3, max = 10, message = "Origin city code must be between 3 and 10 characters")
    @Column(name = "city_code_origin", nullable = false)
    private String cityCodeOrigin;

    @NotBlank(message = "Destination city code cannot be blank")
    @Size(min = 3, max = 10, message = "Destination city code must be between 3 and 10 characters")
    @Column(name = "city_code_destination", nullable = false)
    private String cityCodeDestination;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be a positive value")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Client ID cannot be null")
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @NotNull(message = "Train ID cannot be null")
    @Column(name = "train_id", nullable = false)
    private Long trainId;

    @NotNull(message = "Departure date cannot be null")
    @Column(name = "departure_date", nullable = false)
    private Date departureDate;
}