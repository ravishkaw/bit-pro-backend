package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.TaskTargetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Control task target type related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/task-target-types")
public class TaskTargetTypeController {
    private final TaskTargetTypeService taskTargetTypeService;

    //Fetches a list of task target types.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllTaskTargetTypes() {
        List<ReferenceDataDTO> taskTypes = taskTargetTypeService.getAllTaskTargetTypes();
        return new ResponseEntity<>(taskTypes, HttpStatus.OK);
    }
}
