package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle all task status related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/task-status")
public class TaskStatusController {
    private final TaskStatusService taskStatusService;

    //Fetches a list of task Status.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllTaskStatus() {
        List<ReferenceDataDTO> taskStatus = taskStatusService.getAllTaskStatuses();
        return new ResponseEntity<>(taskStatus, HttpStatus.OK);
    }
}
