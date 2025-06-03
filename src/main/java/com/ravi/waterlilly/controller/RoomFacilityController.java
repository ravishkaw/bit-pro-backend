package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.roomFacility.RoomFacilityPayloadDTO;
import com.ravi.waterlilly.payload.roomFacility.RoomFacilityTableResponse;
import com.ravi.waterlilly.service.RoomFacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handles all room facilities related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-facilities")
public class RoomFacilityController {
    private final RoomFacilityService roomFacilityService;

    // Get all facilities with pagination
    @GetMapping
    public ResponseEntity<RoomFacilityTableResponse> getAllFacilities(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery
    ) {
        RoomFacilityTableResponse facilities = roomFacilityService.getAllFacilities(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(facilities, HttpStatus.OK);
    }

    // get all facilities that active
    @GetMapping("/get-all")
    public ResponseEntity<List<RoomFacilityPayloadDTO>> getFacilities() {
        List<RoomFacilityPayloadDTO> facilityPayloadDTOS = roomFacilityService.getFacilities();
        return new ResponseEntity<>(facilityPayloadDTOS, HttpStatus.OK);
    }

    // Get single facility
    @GetMapping("/{facilityId}")
    public ResponseEntity<RoomFacilityPayloadDTO> getSingleFacility(@PathVariable Integer facilityId) {
        RoomFacilityPayloadDTO facilityBasicDTO = roomFacilityService.getSingleFacility(facilityId);
        return new ResponseEntity<>(facilityBasicDTO, HttpStatus.OK);
    }

    // Add new facility
    @PostMapping
    public ResponseEntity<?> addNewFacility(@Valid @RequestBody RoomFacilityPayloadDTO roomFacilityDTO) {
        roomFacilityService.addNewFacility(roomFacilityDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Update facility
    @PutMapping("/{facilityId}")
    public ResponseEntity<?> updateFacility(@Valid @RequestBody RoomFacilityPayloadDTO roomFacilityDTO, @PathVariable Integer facilityId) {
        roomFacilityService.updateFacility(roomFacilityDTO, facilityId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Delete facility
    @DeleteMapping("/{facilityId}")
    public ResponseEntity<?> deleteFacility(@PathVariable Integer facilityId) {
        roomFacilityService.deleteFacility(facilityId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Restores a deleted roomFacility.
    @PutMapping("/{facilityId}/restore")
    public ResponseEntity<?> restoreRoomFacility(@PathVariable Integer facilityId) {
        roomFacilityService.restoreRoomFacility(facilityId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}