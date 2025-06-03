package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Task;
import com.ravi.waterlilly.payload.task.TaskPageResponse;
import com.ravi.waterlilly.payload.task.TaskPayloadDTO;

// Service interface for managing task service.
public interface TaskService {

    // get all tasks
    TaskPageResponse getAllTasks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get single task
    TaskPayloadDTO getSingleTask(Long taskId);

    // create a new task
    void createTask(TaskPayloadDTO taskPayloadDTO);

    // update an existing task
    void updateTask(Long taskId, TaskPayloadDTO taskPayloadDTO);

    // delete an existing task
    void deleteTask(Long taskId);

    //  method to fetch task by id
    Task getTaskById(Long taskId);

    // method to create task for rooms when checking out
    void addTaskWhenRoomCheckingOut(Long roomId);
}
