package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.EventTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/event-types")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    // get all event types
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllEventTypes() {
        List<ReferenceDataDTO> eventTypes = eventTypeService.getAllEventTypes();
        return new ResponseEntity<>(eventTypes, HttpStatus.OK);
    }
}
