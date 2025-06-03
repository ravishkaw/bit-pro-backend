package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Task;
import com.ravi.waterlilly.model.TaskStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing task status service.
public interface TaskStatusService {

    //Fetches a list of task status.
    List<ReferenceDataDTO> getAllTaskStatuses();

    // get task status by id
    TaskStatus getTaskStatusById(Integer taskStatusId);

    // get task cancelled status
    void setTaskCancelledStatus(Long taskId);
}
