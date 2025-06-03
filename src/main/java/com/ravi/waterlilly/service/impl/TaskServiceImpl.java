package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.event.TaskCreatedEvent;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.inventory.InventoryQuantityDTO;
import com.ravi.waterlilly.payload.task.*;
import com.ravi.waterlilly.repository.*;
import com.ravi.waterlilly.service.*;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

// Implementation of Task Service.
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final PrivilegeUtils privilegeUtils;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RoomStatusService roomStatusService;
    private final EventVenueStatusService eventVenueStatusService;
    private final InventoryService inventoryService;
    private final TaskTypeService taskTypeService;
    private final TaskTargetTypeService taskTargetTypeService;
    private final TaskStatusService taskStatusService;
    private final RoomService roomService;
    private final EventVenueService eventVenueService;
    private final EmployeeService employeeService;

    // get all tasks
    @Override
    public TaskPageResponse getAllTasks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.SELECT);

        // sort and pagination
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<Task> taskPage = StringUtils.hasText(searchQuery)
                ? taskRepository.searchTasks(searchQuery.trim(), pageable)
                : taskRepository.findAll(pageable);

        List<Task> tasks = taskPage.getContent();

        List<TaskTableData> tableData = tasks.stream()
                .map(task -> {
                    TaskTableData taskTableData = modelMapper.map(task, TaskTableData.class);
                    if (task.getTargetType().getName().equalsIgnoreCase("Room")) {
                        taskTableData.setTargetName(roomService.getRoomById(task.getTargetId()).getNumber().toString());
                    } else if (task.getTargetType().getName().equalsIgnoreCase("Event Venue")) {
                        taskTableData.setTargetName(eventVenueService.getVenueById(task.getTargetId()).getName());
                    } else {
                        taskTableData.setTargetName("Unknown");
                    }
                    return taskTableData;
                })
                .toList();

        return new TaskPageResponse(
                tableData,
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages(),
                taskPage.isLast()
        );
    }

    // fetch task by id.
    @Override
    public TaskPayloadDTO getSingleTask(Long taskId) {

        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.SELECT);
        Task existingTask = getTaskById(taskId);

        TaskPayloadDTO taskPayloadDTO = modelMapper.map(existingTask, TaskPayloadDTO.class);

        List<InventoryQuantityDTO> inventoryDTOs = existingTask.getTaskInventory().stream()
                .map(inventory -> {
                    InventoryQuantityDTO inventoryDTO = new InventoryQuantityDTO();
                    inventoryDTO.setInventoryId(inventory.getInventory().getId());
                    inventoryDTO.setQuantity(inventory.getQuantity());
                    return inventoryDTO;
                })
                .toList();

        List<TaskCostDTO> costDTOS = existingTask.getTaskCost().stream()
                .map(costs -> {
                    TaskCostDTO costDTO = new TaskCostDTO();
                    costDTO.setCostType(costs.getCostType());
                    costDTO.setAmount(costs.getAmount());
                    return costDTO;
                })
                .toList();

        taskPayloadDTO.setTaskInventories(inventoryDTOs);
        taskPayloadDTO.setTaskCosts(costDTOS);

        return taskPayloadDTO;
    }


    // create task.
    @Transactional
    @Override
    public void createTask(TaskPayloadDTO taskPayloadDTO) {
        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.INSERT);

        // validate if any task is there
        validateUniqueness(taskPayloadDTO, null);

        Task task = new Task();
        populateTaskFields(task, taskPayloadDTO);

        updateTargetTypeStatus(task);

        Task savedTask = taskRepository.save(task);
        publishTaskCreatedEvent(savedTask);
    }

    // update task.
    @Transactional
    @Override
    public void updateTask(Long taskId, TaskPayloadDTO taskPayloadDTO) {
        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.UPDATE);

        Task existingTask = getTaskById(taskId);
        validateUniqueness(taskPayloadDTO, existingTask.getId());

        // Update the task fields
        populateTaskFields(existingTask, taskPayloadDTO);

        updateTargetTypeStatus(existingTask);

        taskRepository.save(existingTask);
    }

    // delete task.
    @Override
    public void deleteTask(Long taskId) {
        // check privileges
        privilegeUtils.privilegeCheck("Task", AppConstants.DELETE);
        taskStatusService.setTaskCancelledStatus(taskId);
    }

    // method to get task by id
    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));
    }

    // method to create task for rooms when checking out
    @Override
    public void addTaskWhenRoomCheckingOut(Long roomId) {
        TaskPayloadDTO taskPayloadDTO = new TaskPayloadDTO();
        taskPayloadDTO.setTargetTypeId(1); // 1 - Room 2 - Event Venue
        taskPayloadDTO.setTargetId(roomId);
        taskPayloadDTO.setScheduledStartTime(LocalDateTime.now());
        taskPayloadDTO.setScheduledEndTime(LocalDateTime.now().plusMinutes(45));
        taskPayloadDTO.setAssignedToId(null);
        taskPayloadDTO.setDescription("cleaning");
        taskPayloadDTO.setTaskStatusId(1);
        taskPayloadDTO.setTaskTypeId(1);
        createTask(taskPayloadDTO);
    }

    // Helper method to populate task fields
    private void populateTaskFields(Task task, TaskPayloadDTO dto) {
        task.setTargetType(taskTargetTypeService.getTaskTargetTypeById(dto.getTargetTypeId()));
        task.setTargetId(dto.getTargetId());
        task.setAssignedTo(updateEmployee(dto.getAssignedToId()));
        task.setDescription(dto.getDescription());
        task.setScheduledStartTime(dto.getScheduledStartTime());
        task.setScheduledEndTime(dto.getScheduledEndTime());
        task.setActualStartTime(dto.getActualStartTime());
        task.setActualEndTime(dto.getActualEndTime());
        task.setTaskStatus(taskStatusService.getTaskStatusById(dto.getTaskStatusId()));
        task.setTaskType(taskTypeService.getTaskTypeById(dto.getTaskTypeId()));

        LocalDateTime now = LocalDateTime.now();
        if (task.getId() == null) {
            task.setAddedDateTime(now);
        }
        task.setLastModifiedDateTime(now);

        updateInventories(task, dto);
        updateTaskCost(task, dto);
    }

    // Helper method to update employee
    private Employee updateEmployee(Long employeeId) {
        if (employeeId == null) {
            return null;
        }
        return employeeService.getEmployeeById(employeeId);
    }

    // Helper method to update inventories
    private void updateInventories(Task task, TaskPayloadDTO dto) {
        if (dto.getTaskInventories() == null) {
            return;
        }

        if (task.getTaskInventory() != null && !task.getTaskInventory().isEmpty()) {
            task.clearInventory();
        }

        dto.getTaskInventories().forEach(inventoryDTO -> {
            Inventory inventory = inventoryService.getInventoryById(inventoryDTO.getInventoryId());
            task.addInventory(inventory, inventoryDTO.getQuantity());
        });

        inventoryService.reserveInventory(dto.getTaskInventories());
    }

    // Helper method to update task costs
    private static void updateTaskCost(Task task, TaskPayloadDTO dto) {
        if (dto.getTaskCosts() == null) {
            return;
        }

        if (task.getTaskCost() != null && !task.getTaskCost().isEmpty()) {
            task.clearCost();
        }

        dto.getTaskCosts().forEach(costDTO -> {
            TaskCost taskCost = new TaskCost();
            taskCost.setTask(task);
            taskCost.setCostType(costDTO.getCostType());
            taskCost.setAmount(costDTO.getAmount());
            task.addCost(taskCost);
        });
    }

    // Helper method to update target type status
    private void updateTargetTypeStatus(Task task) {
        String taskStatusName = task.getTaskStatus().getName();
        String taskTypeName = task.getTaskType().getName();

        if ("Room".equalsIgnoreCase(task.getTargetType().getName())) {
            roomStatusService.updateStatusBasedOnTask(task.getTargetId(), taskStatusName, taskTypeName);
        } else if ("Event Venue".equalsIgnoreCase(task.getTargetType().getName())) {
            eventVenueStatusService.updateStatusBasedOnTask(task.getTargetId(), taskStatusName, taskTypeName);
        }
    }

    // check if there is already a task for the room
    private void validateUniqueness(TaskPayloadDTO dto, Long taskId) {
        TaskTargetType targetType = taskTargetTypeService.getTaskTargetTypeById(dto.getTargetTypeId());
        Long targetId = dto.getTargetId();
        Integer taskTypeId = dto.getTaskTypeId();
        boolean taskExists;

        if (taskId == null) {
            taskExists = taskRepository.checkIfTasksExists(targetId, targetType.getId(), taskTypeId);
        } else {
            taskExists = taskRepository.checkIfTasksExistsExcludingId(targetId, targetType.getId(), taskTypeId, taskId);
        }

        if (taskExists) {
            TaskType taskType = taskTypeService.getTaskTypeById(taskTypeId);
            String targetName = "Room".equalsIgnoreCase(targetType.getName()) ? "room" : "venue";
            throw new APIException("The " + targetName + " has upcoming " + taskType.getName() + " tasks.");
        }
    }

    // Helper method to get target name
    private String getTargetName(Task task) {
        return "Room".equalsIgnoreCase(task.getTargetType().getName())
                ? roomService.getRoomById(task.getTargetId()).getNumber().toString()
                : eventVenueService.getVenueById(task.getTargetId()).getName();
    }

    // publish task created event / notification
    private void publishTaskCreatedEvent(Task task) {
        String targetName = getTargetName(task);
        TaskCreatedEvent event = new TaskCreatedEvent(
                this, task, task.getTargetType().getName(), targetName
        );
        applicationEventPublisher.publishEvent(event);
    }
}