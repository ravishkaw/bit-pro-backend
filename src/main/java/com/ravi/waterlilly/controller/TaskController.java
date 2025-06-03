package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.task.TaskPageResponse;
import com.ravi.waterlilly.payload.task.TaskPayloadDTO;
import com.ravi.waterlilly.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// handle all task related api requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    // get all tasks
    @GetMapping
    public ResponseEntity<TaskPageResponse> getAllTasks(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery
    ) {
        TaskPageResponse response = taskService.getAllTasks(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get single task
    @GetMapping("/{id}")
    public ResponseEntity<TaskPayloadDTO> getSingleTask(@PathVariable Long id) {
        TaskPayloadDTO payloadDTO = taskService.getSingleTask(id);
        return new ResponseEntity<>(payloadDTO, HttpStatus.OK);
    }

    // add new task
    @PostMapping
    public ResponseEntity<?> addNewTask(@RequestBody TaskPayloadDTO payloadDTO) {
        taskService.createTask(payloadDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update task
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@RequestBody TaskPayloadDTO payloadDTO, @PathVariable Long id) {
        taskService.updateTask(id, payloadDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
