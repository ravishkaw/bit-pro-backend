package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.IDTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle id types related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/id-types")
public class IDTypeController {
    private final IDTypeService idTypeService;

    //Fetches a list of id types.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllDesignations() {
        List<ReferenceDataDTO> idTypeDTOS = idTypeService.getAllIDTypes();
        return new ResponseEntity<>(idTypeDTOS, HttpStatus.OK);
    }
}
