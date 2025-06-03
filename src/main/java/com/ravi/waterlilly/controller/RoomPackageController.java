package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.roomPackage.RoomPackageBasicDTO;
import com.ravi.waterlilly.payload.roomPackage.RoomPackagePageResponse;
import com.ravi.waterlilly.payload.roomPackage.RoomPackagePayloadDTO;
import com.ravi.waterlilly.service.RoomPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handle package-related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-packages")
public class RoomPackageController {
    private final RoomPackageService roomPackageService;

    // get all room packages
    @GetMapping
    public ResponseEntity<RoomPackagePageResponse> getAllRoomPackages(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery) {
        RoomPackagePageResponse response = roomPackageService.getAllRoomPackages(
                pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return ResponseEntity.ok(response);
    }

    // get all room packages without pagination
    @GetMapping("/get-all")
    public ResponseEntity<List<RoomPackageBasicDTO>> getAllRoomPackagesNoPagination() {
        List<RoomPackageBasicDTO> roomPackages = roomPackageService.getAllRoomPackagesNoPagination();
        return new ResponseEntity<>(roomPackages, HttpStatus.OK);
    }

    // get single room package
    @GetMapping("/{packageId}")
    public ResponseEntity<RoomPackagePayloadDTO> getRoomPackageById(@PathVariable Integer packageId) {
        RoomPackagePayloadDTO roomPackage = roomPackageService.getSingleRoomPackage(packageId);
        return ResponseEntity.ok(roomPackage);
    }

    // add new room package
    @PostMapping
    public ResponseEntity<RoomPackagePayloadDTO> createRoomPackage(@RequestBody RoomPackagePayloadDTO packageDTO) {
        roomPackageService.createRoomPackage(packageDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update room package
    @PutMapping("/{packageId}")
    public ResponseEntity<RoomPackagePayloadDTO> updateRoomPackage(@PathVariable Integer packageId, @RequestBody RoomPackagePayloadDTO packageDTO) {
        roomPackageService.updateRoomPackage(packageId, packageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete room package
    @DeleteMapping("/{packageId}")
    public ResponseEntity<Void> deleteRoomPackage(@PathVariable Integer packageId) {
        roomPackageService.deleteRoomPackage(packageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // restore a room pacakge
    @PutMapping("/{packageId}/restore")
    public ResponseEntity<Void> restoreRoomPackage(@PathVariable Integer packageId) {
        roomPackageService.restoreRoomPackage(packageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}