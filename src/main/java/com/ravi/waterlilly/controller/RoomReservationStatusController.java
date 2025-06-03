package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.RoomReservationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Handle all room reservation status API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-reservation-status")
public class RoomReservationStatusController {
    private final RoomReservationStatusService roomReservationStatusService;

    // get all room reservation status
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllStatus() {
        List<ReferenceDataDTO> statusDTOS = roomReservationStatusService.getAllRoomReservationStatus();
        return new ResponseEntity<>(statusDTOS, HttpStatus.OK);
    }
}
