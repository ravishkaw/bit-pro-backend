package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.RoomStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handles all room status -related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-status")
public class RoomStatusController {
    private final RoomStatusService statusService;

    // Fetches a list of room status.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllStatus() {
        List<ReferenceDataDTO> statusList = statusService.getAllStatus();
        return new ResponseEntity<>(statusList, HttpStatus.OK);
    }
}
