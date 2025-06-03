package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.IDTypeService;
import com.ravi.waterlilly.service.RoomReservationSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle room reservation source related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-reservation-sources")
public class RoomReservationSourceController {
    private final RoomReservationSourceService roomReservationSourceService;

    //Fetches a list of room reservation sources.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllSources() {
        List<ReferenceDataDTO> dataDTO = roomReservationSourceService.getAllRoomReservationSources();
        return new ResponseEntity<>(dataDTO, HttpStatus.OK);
    }
}
