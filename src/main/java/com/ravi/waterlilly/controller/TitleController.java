package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handles all title -related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/titles")
public class TitleController {
    private final TitleService titleService;

    // Fetches a list of title.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllTitles() {
        List<ReferenceDataDTO> titleDTOS = titleService.getAllTitles();
        return new ResponseEntity<>(titleDTOS, HttpStatus.OK);
    }
}
