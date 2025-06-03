package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.EventVenueStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handles all event venue status -related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/event-venue-status")
public class EventVenueStatusController {
    private final EventVenueStatusService eventVenueStatusService;

    // Fetches a list of status.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllStatus() {
        List<ReferenceDataDTO> statusList = eventVenueStatusService.getAllStatus();
        return new ResponseEntity<>(statusList, HttpStatus.OK);
    }
}
