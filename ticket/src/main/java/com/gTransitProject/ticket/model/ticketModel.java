package com.gTransitProject.ticket.model;

import java.sql.Date;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "ticket")
@NoArgsConstructor
@AllArgsConstructor
public class ticketModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    @NotNull(message = "Code cannot be null. (Formatting is weird but just do initials of two cities, i guess")
    @Column(unique = true, length = 7, nullable = false) //imagine.. LANY001 for Los Angeles --> New York
    private String code;

    @NotNull(message = "Economically, we can't have this be null")
    @Column(unique = false, nullable = true)
    @Min(value = 1, message = "You want this to less than one? Are you insane?")
    @Max(value = 1260000000, message = "I mean, if you want to pay this, sure, i'm not complaining but LEGALLY i cannot") //1.26 billion, 1.26e9
    private Integer price;

    @Column(nullable = false, unique=false)
    @NotNull(message = "We do need to know which city you wanted to go to.") 
    //This WORKS because there are stations everywhere and there could be many cities with the same station name, hypothetically.
    private String cityCodeOriginDestination;

    @NotNull(message = "I do need a destination somewhere")
    @Column(unique = false, nullable = true, name="destination")
    @Length(max=20, min=2)
    private String uniqueStationCodeDestination; //Obtain from station table

    @Column(nullable = false, unique=false)
    @NotNull(message = "We do need to know which city you left from.")
    private String cityCodeOrigin;

    @NotNull(message = "I do need a destination somewhere")
    @Column(unique = false, nullable = true, name="origin")
    @Length(max=20, min=2)
    private String uniqueStationCodeOrigin; //Obtain from station table

    @Column(nullable = false)
    @NotNull(message = "What are you even riding? The air? We NEED A line number to keep track of.")
    private Integer lineNumber;

    @NotNull(message = "Code cannot be null. We need to know the unique identifier for this train.")
    @Column(unique = true, length = 5, nullable = false)
    private String trainCode; //extract from train

    @NotNull(message = "Code cannot be null. We do need to know who you are.")
    @Column(unique = true, length = 10, nullable = false)
    private String clientId; //extract from client. Can be either a person or a company i suppose

    @NotNull(message = "Code cannot be null. We do need to know drove you there.")
    @Column(unique = false, length = 10, nullable = false)
    private String driverId; //extract from driver

    @NotNull(message = "Would be nice to know when you're leaving")
    @Column(unique = false, nullable = false)
    private Date departureTime; //extract from client

    @NotNull(message = "Would be nice to know when you're arriving though")
    @Column(unique = false, nullable = false)
    private Date arrivalTime; //extract from client

    @NotBlank(message = "At least type null")
    @Column(unique = false, nullable = false)
    private Integer kgCargo; //Cargo used for both client or company i suppose

    //todo: make sure arrival time doesn't go before departure time because that violates the laws of physics and the universe itself
    //also make sure you can't have a client that can go in the dedicated cargo-type trains because HOW would that work?
}
