package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.BedTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// this controller handles all bed type related api
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/bed-types")
public class BedTypeController {
    private final BedTypeService bedTypeService;

    // get all bed types
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllBedTypes() {
        List<ReferenceDataDTO> bedTypeDTOS = bedTypeService.getAllBedTypes();
        return new ResponseEntity<>(bedTypeDTOS, HttpStatus.OK);
    }

    // get single bed type
    @GetMapping("/{bedTypeId}")
    public ResponseEntity<ReferenceDataDTO> getBedTypeById(@PathVariable Integer bedTypeId) {
        ReferenceDataDTO bedTypeDTO = bedTypeService.getSingleBedType(bedTypeId);
        return new ResponseEntity<>(bedTypeDTO, HttpStatus.OK);
    }

    // add a bed type
    @PostMapping
    public ResponseEntity<?> addBedType(@RequestBody ReferenceDataDTO bedTypeDTO) {
        bedTypeService.addNewBedType(bedTypeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // update a bed type
    @PutMapping("/{bedTypeId}")
    public ResponseEntity<?> updateBedType(@PathVariable Integer bedTypeId, @RequestBody ReferenceDataDTO bedTypeDTO) {
        bedTypeService.updateBedType(bedTypeDTO, bedTypeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a bed type
    @DeleteMapping("/{bedTypeId}")
    public ResponseEntity<?> deleteBedType(@PathVariable Integer bedTypeId) {
        bedTypeService.deleteBedType(bedTypeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
