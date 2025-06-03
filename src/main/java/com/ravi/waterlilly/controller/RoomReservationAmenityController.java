package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.roomReservationAmenity.RoomReservationAmenityPageResponse;
import com.ravi.waterlilly.payload.roomReservationAmenity.RoomReservationAmenityPayloadDTO;
import com.ravi.waterlilly.payload.roomReservationAmenity.RoomReservationAmenityTableData;
import com.ravi.waterlilly.service.RoomReservationAmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handle all room reservation amenity related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-reservation-amenities")
public class RoomReservationAmenityController {
    private final RoomReservationAmenityService roomReservationAmenityService;

    //get all
    @GetMapping
    public ResponseEntity<RoomReservationAmenityPageResponse> getAll(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery) {
        RoomReservationAmenityPageResponse amenitiesDTOS = roomReservationAmenityService.getAll(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(amenitiesDTOS, HttpStatus.OK);
    }

    // get all without pagination
    @GetMapping("/get-all")
    public ResponseEntity<List<RoomReservationAmenityTableData>> getRoomReservationAmenities() {
        List<RoomReservationAmenityTableData> allData = roomReservationAmenityService.getRoomReservationAmenities();
        return new ResponseEntity<>(allData, HttpStatus.OK);
    }

    // get single amenity
    @GetMapping("/{amenityId}")
    public ResponseEntity<RoomReservationAmenityPayloadDTO> getSingleAmenity(@PathVariable Integer amenityId) {
        RoomReservationAmenityPayloadDTO amenityPayloadDTO = roomReservationAmenityService.getSingleAmenity(amenityId);
        return new ResponseEntity<>(amenityPayloadDTO, HttpStatus.OK);
    }

    // add new amenity
    @PostMapping
    public ResponseEntity<?> createRoomReservationAmenity(@RequestBody RoomReservationAmenityPayloadDTO amenityPayloadDTO) {
        roomReservationAmenityService.addRoomReservationAmenity(amenityPayloadDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update amenity
    @PutMapping("/{amenityId}")
    public ResponseEntity<?> updateRoomReservationAmenity(
            @RequestBody RoomReservationAmenityPayloadDTO amenityPayloadDTO,
            @PathVariable Integer amenityId
    ) {
        roomReservationAmenityService.updateRoomReservationAmenity(amenityPayloadDTO, amenityId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete amenity
    @DeleteMapping("/{amenityId}")
    public ResponseEntity<?> deleteRoomReservationAmenity(@PathVariable Integer amenityId) {
        roomReservationAmenityService.deleteRoomReservationAmenity(amenityId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // restore amenity
    @PutMapping("/{amenityId}/restore")
    public ResponseEntity<?> restoreRoomReservationAmenity(@PathVariable Integer amenityId) {
        roomReservationAmenityService.restoreRoomReservationAmenity(amenityId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
