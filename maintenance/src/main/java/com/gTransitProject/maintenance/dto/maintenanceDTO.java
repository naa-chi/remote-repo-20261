package com.gTransitProject.maintenance.dto;
import com.gTransitProject.maintenance.model.maintenanceModel;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class maintenanceDTO {
    private Integer id;
    private Date entry_date_of_maintenance;
    private Date leave_date_of_maintenance;
    private String issue_description;
    private String report_description;
    private Integer maintenance_price;
    private Integer vehicleId; 
    private String model; 
    private Integer risk_importance;
    private Integer responsible_maintanance_crew;
    /*
    
    hides the maintenance crew responsible for the maintenance, 
    since it's irrelevant to the user 
    (and could be a security risk to show it i guess)
    */
   
    public maintenanceModel toModel() {
        return new maintenanceModel(
            this.id,
            this.entry_date_of_maintenance,
            this.leave_date_of_maintenance,
            this.issue_description,
            this.report_description,
            this.maintenance_price,
            this.vehicleId,
            this.model,
            this.risk_importance,
            this.responsible_maintanance_crew
        );
    }

    public static maintenanceDTO fromModel(maintenanceModel model) {
        return new maintenanceDTO(
            model.getId(),
            model.getEntry_date_of_maintenance(),
            model.getLeave_date_of_maintenance(),
            model.getIssue_description(),
            model.getReport_description(),
            model.getMaintenance_price(),
            model.getVehicleId(),
            model.getModel(),
            model.getRisk_importance(),
            null //keep it hidden,,,,
        );
    }
}
