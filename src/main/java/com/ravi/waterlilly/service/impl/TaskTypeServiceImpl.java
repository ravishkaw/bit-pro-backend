package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.TaskType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.TaskTypeRepository;
import com.ravi.waterlilly.service.TaskTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of task type service
@Service
@RequiredArgsConstructor
public class TaskTypeServiceImpl implements TaskTypeService {
    private final ModelMapper modelMapper;
    private final TaskTypeRepository taskTypeRepository;

    // fetch all task types
    @Override
    public List<ReferenceDataDTO> getAllTaskTypes() {
        return taskTypeRepository.findAll().stream()
                .map((taskType -> modelMapper.map(taskType, ReferenceDataDTO.class)))
                .toList();
    }

    // get task type by id
    @Override
    public TaskType getTaskTypeById(Integer id) {
        return taskTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task type", "id", id.toString()));
    }
}
