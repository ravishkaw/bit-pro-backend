package com.ravi.waterlilly.payload.task;

import com.ravi.waterlilly.payload.inventory.InventoryQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

// payload of task
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskPayloadDTO {
    private Long id;
    private Integer targetTypeId;
    private Long targetId;
    private Long assignedToId;
    private String description;
    private LocalDateTime scheduledStartTime;
    private LocalDateTime scheduledEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private LocalDateTime addedDateTime;
    private LocalDateTime lastModifiedDateTime;
    private Integer taskStatusId;
    private Integer taskTypeId;
    private List<InventoryQuantityDTO> taskInventories;
    private List<TaskCostDTO> taskCosts;
}
