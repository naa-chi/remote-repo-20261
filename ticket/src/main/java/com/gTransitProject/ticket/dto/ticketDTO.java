package com.gTransitProject.ticket.dto;
import lombok.Data;

@Data
public class ticketDTO {
    private Integer clientId;
    private Integer specificTrainId;
    private String route;
    private String departureTime;
    private String arrivalTime;
}
