package com.ravi.waterlilly.payload.preventiveMaintenance;

import com.ravi.waterlilly.model.TaskTargetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// preventive maintenance payload dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreventiveMaintenancePayloadDTO {
    private Integer id;
    private Integer targetTypeId;
    private Long targetId;
    private String maintenanceType;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private Integer maintenanceStatusId;
}
