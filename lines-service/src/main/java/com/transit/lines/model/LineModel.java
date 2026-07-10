package com.transit.lines.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "line", schema = "transport_db_linesservice")
public class LineModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "line_code", nullable = false, unique = true)
    private Integer lineCode;

    @NotNull
    @Column(name = "line_length", nullable = false, unique = false)
    @Min(value = 0)
    @Max(value = 999999999)
    private Long lineLengthKM;

    @NotNull
    @Column(name = "line_people_served", nullable = false, unique = false)
    private Long peopleServedMonthlyEstimate; //needed to pad out this model ngl
}
