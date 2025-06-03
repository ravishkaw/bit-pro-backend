package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Control designation related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/designations")
public class DesignationController {
    private final DesignationService designationService;

    //Fetches a list of designations.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllDesignations() {
        List<ReferenceDataDTO> designationDTOS = designationService.getAllDesignations();
        return new ResponseEntity<>(designationDTOS, HttpStatus.OK);
    }
}
