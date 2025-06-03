package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.TaskTargetType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.TaskTargetTypeRepository;
import com.ravi.waterlilly.service.TaskTargetTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of task type service
@Service
@RequiredArgsConstructor
public class TaskTargetTypeServiceImpl implements TaskTargetTypeService {
    private final ModelMapper modelMapper;
    private final TaskTargetTypeRepository taskTargetTypeRepository;

    // fetch all task target types
    @Override
    public List<ReferenceDataDTO> getAllTaskTargetTypes() {
        return taskTargetTypeRepository.findAll().stream()
                .map((taskTargetType -> modelMapper.map(taskTargetType, ReferenceDataDTO.class)))
                .toList();
    }

    // get task target type by id
    @Override
    public TaskTargetType getTaskTargetTypeById(Integer taskTargetTypeId) {
        return taskTargetTypeRepository.findById(taskTargetTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Task target type", "id", taskTargetTypeId.toString()));
    }
}
