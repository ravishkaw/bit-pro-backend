package com.ravi.waterlilly.payload.preventiveMaintenance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


// preventive maintenance table data dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreventiveMaintenanceTableDataDTO {
    private Integer id;
    private String targetTypeName;
    private String targetName;
    private String maintenanceType;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private String maintenanceStatusName;
}
