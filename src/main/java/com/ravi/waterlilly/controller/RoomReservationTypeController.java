package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.CivilStatusService;
import com.ravi.waterlilly.service.RoomReservationTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle Civil Status related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-reservation-types")
public class RoomReservationTypeController {
    private final RoomReservationTypeService reservationTypeService;

    //Fetches a list of room reservation types
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllRoomReservationTypes() {
        List<ReferenceDataDTO> referenceDataDTOS = reservationTypeService.getAllRoomReservationTypes();
        return new ResponseEntity<>(referenceDataDTOS, HttpStatus.OK);
    }
}
