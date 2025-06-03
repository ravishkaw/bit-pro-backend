package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.CivilStatusService;
import com.ravi.waterlilly.service.PreventiveMaintenanceStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle all preventive maintenance status related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/preventive-maintenance-status")
public class PreventiveMaintenanceStatusController {

    private final PreventiveMaintenanceStatusService preventiveMaintenanceStatusService;

    //Fetches a list of Status.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllStatus() {
        List<ReferenceDataDTO> status = preventiveMaintenanceStatusService.getAllStatus();
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
