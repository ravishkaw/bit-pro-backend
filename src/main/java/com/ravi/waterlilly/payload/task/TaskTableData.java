package com.ravi.waterlilly.payload.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// table data for task
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskTableData {
    private Long id;
    private String targetTypeName;
    private String targetName;
    private String assignedToCallingName;
    private String description;
    private LocalDateTime scheduledStartTime;
    private LocalDateTime scheduledEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private String taskStatusName;
    private String taskTypeName;
}
