package com.gTransitProject.line.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Entity
@Table(name = "tb_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Line Number cannot be null. We need to know the unique identifier for this line.")
    private Integer lineNumber;

    @Column(unique = false, nullable = false)
    @NotNull(message = "Length in Km cannot be null. We need to know the length of this line.")
    @Min(value = 1, message = "Doesn't make sense to be smaller than 1000m.")
    @Max(value = 150000000, message = "This would be larger than, literally, the distance between the SUN and the EARTH. What are you doing?")
    private Integer lengthInKm;
}

