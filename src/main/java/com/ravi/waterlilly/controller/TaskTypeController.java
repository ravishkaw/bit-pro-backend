package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.CivilStatusService;
import com.ravi.waterlilly.service.TaskTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Control task type related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/task-types")
public class TaskTypeController {
    private final TaskTypeService taskTypeService;

    //Fetches a list of task types.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllTaskTypes() {
        List<ReferenceDataDTO> taskTypes = taskTypeService.getAllTaskTypes();
        return new ResponseEntity<>(taskTypes, HttpStatus.OK);
    }
}
