package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.TaskTargetType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing task target type service.
public interface TaskTargetTypeService {

    //Fetches a list of task target types.
    List<ReferenceDataDTO> getAllTaskTargetTypes();

    // get task target type by id
    TaskTargetType getTaskTargetTypeById(Integer taskTargetTypeId);
}
