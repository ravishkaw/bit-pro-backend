package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.GenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle gender related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/genders")
public class GenderController {
    private final GenderService genderService;

    //Fetches a list of genders.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllDesignations() {
        List<ReferenceDataDTO> genders = genderService.getAllGenders();
        return new ResponseEntity<>(genders, HttpStatus.OK);
    }
}
