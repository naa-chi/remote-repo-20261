package com.gTransitProject.maintenance.model;

//This is the part that allows data to get saved and whatnot

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import jakarta.validation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


//This is the actual data

@SuppressWarnings("unused")
@Entity
@Table(name = "maintenance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class maintenanceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //It WOULD make sense to link this to another table methinks

    @NotNull(message = "What do you MEAN you have a null date???")
    @Column(nullable = false, unique = false)
    private Date entry_date_of_maintenance;

    @NotNull(message = "Again, date cannot be null")
    @Column(nullable = false, unique = false)
    private Date leave_date_of_maintenance;

    @NotNull(message = "We can't just have a blank issue description")
    @Column(nullable = false, unique=false)
    private String issue_description;

    @NotNull(message = "Report description cannot be null. How are people supposed to know what you did?")
    @Column(nullable = false, unique=false)
    private String report_description;

    @NotNull(message = "Maintenance price cannot be null. Labour and parts cost money, you know.")
    @Column(nullable = false, unique = false)
    private Integer maintenance_price;

    @NotNull(message = "We need to know which vehicle is being maintained.")
    @Column(nullable = false, unique = false)
    private Integer vehicleId; //linked to the actual train/engine, not the type of it y'know.

    @NotNull(message = "Model cannot be null. We need to know what we're working on.")
    @Column(nullable = false, unique = false)
    private String model; 
    /*

    model of the actual THING being sent to maintenance (train, engine, etc) 
    (Could be two separate tables being on the same thing??? does it make sense??? 
    from a model perspective, no, but from a business perspective yes)

    */

    @NotNull(message = "Risk importance cannot be null. We need to know how urgent this is.")
    @Column(nullable = false, unique = false)
    @Min(value = 0)
    @Max(value = 3)
    private Integer risk_importance; 
    
    //0 = low priority, 3 = fix ASAP
    //lgtm

    @NotNull(message = "Responsible maintenance crew cannot be null. We need to know who is handling this.")
    @Column(nullable = false, unique = false)
    @Min(value = 0)
    @Max(value = 32)
    private Integer responsible_maintanance_crew;
    //Secondary table in the same maintenance microservice? Would make sense
    
}

