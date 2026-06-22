package com.gTransitProject.review.model;
import java.sql.Date;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
// Specifying the schema for the manufacturer table
@Table(name = "review") 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class reviewModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = false)
    @NotNull(message = "How do we know what client left this review?")
    private String clientId; 
    //Technically a client can have multiple reviews 
    // depending on ticket if it's not stored as a 
    // monolithic review for the entire trip/service

    @Column(nullable = false, unique = false)
    @NotNull(message = "How do we know which train this review is for?")
    private String specificTrainId;
    //From the train table

    @Column(nullable = true, unique = false)
    private String seatNumber;
    //Can be cargo, thereby nullable.

    @Column(nullable = false, unique = false)
    @NotNull(message = "How do we know the review content?")
    @Length(max = 250, message = "Reviews are constrainted to <250 characters.")
    @Length(min = 3, message = "Reviews must be at least 3 characters long.") 
    private String reviewContent;
    //3 is the minimum because the only constructive review you can give
    //at 3 characters is just 'bad' lmao. lol, even.

    @Column(nullable = false, unique = false)
    @NotNull(message = "How do we know the rating?")
    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating cannot be more than 5.")
    private Integer rating;

    @Column(nullable = false, unique = false)
    @NotNull(message = "How do we know the date of the review?")
    private Date reviewDate;

    @Column(nullable = false, unique = false)
    @NotNull(message = "How do we know route of the service this review is for?")
    @Length(max = 7, message = "Route code must be less than 7 characters.")
    private String route; //take from Ticket.
    //The route also stores the start and end destinations, 
    //so it is irrelevant and unnecessary to have them also be in this model.

    /* 
    @Column(unique = false, length = 7, nullable = false) 
    //imagine.. LANY001 for Los Angeles --> New York
    private String code;
    */

    @Column(nullable = false, unique = true)
    @NotNull(message = "How do we know the ticket ID that this review is for?")
    private String ticketId; 

    /*
    From the Ticket table too.
    
    */

    @NotNull(message = "Code cannot be null. (Formatting is weird but just do initials of two cities, i guess")
    @Column(unique = false, nullable=false)
    private String driverInCharge;
    //from the driver table as we can very easily guess.


}
