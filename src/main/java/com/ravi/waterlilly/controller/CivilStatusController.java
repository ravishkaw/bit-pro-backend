package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.CivilStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Control Civil Status related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/civil-status")
public class CivilStatusController {
    private final CivilStatusService civilStatusService;

    //Fetches a list of Civil Status.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllCivilStatus() {
        List<ReferenceDataDTO> civilStatusDTOS = civilStatusService.getAllCivilStatus();
        return new ResponseEntity<>(civilStatusDTOS, HttpStatus.OK);
    }
}
