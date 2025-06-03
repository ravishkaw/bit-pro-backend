package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.TimeUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Control TimeUnit related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/time-units")
public class TimeUnitController {
    private final TimeUnitService timeUnitService;

    //Fetches a list of Time Units.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllTimeUnits() {
        List<ReferenceDataDTO> timeUnitDTOs = timeUnitService.getAllTimeUnits();
        return new ResponseEntity<>(timeUnitDTOs, HttpStatus.OK);
    }
}