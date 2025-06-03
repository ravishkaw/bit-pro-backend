package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.NationalityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle nationalities related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/nationalities")
public class NationalityController {
    private final NationalityService nationalityService;

    //Fetches a list of nationalities.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllDesignations() {
        List<ReferenceDataDTO> nationalities = nationalityService.getAllNationalities();
        return new ResponseEntity<>(nationalities, HttpStatus.OK);
    }
}
