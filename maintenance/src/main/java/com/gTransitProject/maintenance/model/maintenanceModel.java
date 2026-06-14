package com.gTransitProject.maintenance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import jakarta.validation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

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

    @Column(nullable = false, unique = false)
    private Date entry_date_of_maintenance;

    @Column(nullable = false, unique = false)
    private Date leave_date_of_maintenance;

    @Column(nullable = false, unique=false)
    private String issue_description;

    @Column(nullable = false, unique=false)
    private String report_description;

    @Column(nullable = false, unique = false)
    private Integer maintenance_price;

    @Column(nullable = false, unique = false)
    private Integer vehicleId; //linked to the actual train/engine, not the type of it y'know.

    @Column(nullable = false, unique = false)
    private String model; 
    /*

    model of the actual THING being sent to maintenance (train, engine, etc) 
    (Could be two separate tables being on the same thing??? does it make sense??? 
    from a model perspective, no, but from a business perspective yes)

    */

    @Column(nullable = false, unique = false)
    @Min(value = 0)
    @Max(value = 3)
    private Integer risk_importance; 
    
    //0 = low priority, 3 = fix ASAP
    //lgtm

    @Column(nullable = false, unique = false)
    @Min(value = 0)
    @Max(value = 32)
    private Integer responsible_maintanance_crew;
    //Secondary table in the same maintenance microservice? Would make sense
    
}

