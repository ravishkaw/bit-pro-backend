package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Task;
import com.ravi.waterlilly.model.TaskStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.TaskRepository;
import com.ravi.waterlilly.repository.TaskStatusRepository;
import com.ravi.waterlilly.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of task status service
@Service
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {
    private final ModelMapper modelMapper;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskRepository taskRepository;

    // fetch all task status
    @Override
    public List<ReferenceDataDTO> getAllTaskStatuses() {
        return taskStatusRepository.findAll().stream()
                .map((taskStatus -> modelMapper.map(taskStatus, ReferenceDataDTO.class)))
                .toList();
    }

    // get task status by id
    @Override
    public TaskStatus getTaskStatusById(Integer taskStatusId) {
        return taskStatusRepository.findById(taskStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("Task status", "id", taskStatusId.toString()));
    }

    // get task cancelled status
    @Override
    public void setTaskCancelledStatus(Long taskId) {
        Task task = getTask(taskId);
        setTaskStatus(task, "Cancelled");
        taskRepository.save(task);
    }

    // set task status
    private void setTaskStatus(Task task, String statusName) {
        TaskStatus status = taskStatusRepository.findByName(statusName);
        if (status == null) {
            throw new ResourceNotFoundException("Task status", "name", statusName);
        }
        task.setTaskStatus(status);
    }

    // Helper method to get task
    private Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
    }
}
