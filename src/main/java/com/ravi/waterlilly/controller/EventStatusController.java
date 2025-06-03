package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.EventStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle event status related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/event-status")
public class EventStatusController {

    private final EventStatusService eventStatusService;

    // get all event status
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllStatus() {
        List<ReferenceDataDTO> statusDTOS = eventStatusService.getAllStatus();
        return new ResponseEntity<>(statusDTOS, HttpStatus.OK);
    }
}
