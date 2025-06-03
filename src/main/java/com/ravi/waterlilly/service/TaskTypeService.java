package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.TaskType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing task type service.
public interface TaskTypeService {

    //Fetches a list of task types.
    List<ReferenceDataDTO> getAllTaskTypes();

    // get task type by id
    TaskType getTaskTypeById(Integer id);
}
