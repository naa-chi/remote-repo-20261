package com.transit.reviews.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "reviews", schema = "transport_db_reviewsservice")
public class ReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Client ID cannot be null")
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @NotNull(message = "Train ID cannot be null")
    @Column(name = "train_id", nullable = false)
    private Long trainId;

    @NotBlank(message = "Ticket code cannot be blank")
    @Column(name = "ticket_code", nullable = false)
    private String ticketCode;   // <-- references ticket.code

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @NotBlank(message = "Comment cannot be blank")
    @Column(name = "comment", length = 500, nullable = false)
    private String comment;

    @NotNull(message = "Review date cannot be null")
    @Column(name = "review_date", nullable = false)
    private Date reviewDate;
}