package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.eventPackage.EventPackageBasicDTO;
import com.ravi.waterlilly.payload.eventPackage.EventPackagePageResponse;
import com.ravi.waterlilly.payload.eventPackage.EventPackagePayloadDTO;
import com.ravi.waterlilly.service.EventPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/event-packages")
public class EventPackageController {
    private final EventPackageService eventPackageService;

    // get all event packages with pagination and search
    @GetMapping
    public ResponseEntity<EventPackagePageResponse> getAllEventPackages(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery) {
        EventPackagePageResponse response = eventPackageService.getAllEventPackages(
                pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return ResponseEntity.ok(response);
    }

    // get all event packages without pagination
    @GetMapping("/get-all")
    public ResponseEntity<List<EventPackageBasicDTO>> getAllEventPackagesNoPagination() {
        List<EventPackageBasicDTO> eventPackages = eventPackageService.getAllEventPackagesNoPagination();
        return new ResponseEntity<>(eventPackages, HttpStatus.OK);
    }

    // get single event package by id
    @GetMapping("/{packageId}")
    public ResponseEntity<EventPackagePayloadDTO> getEventPackageById(@PathVariable Integer packageId) {
        EventPackagePayloadDTO eventPackage = eventPackageService.getSinglePackage(packageId);
        return ResponseEntity.ok(eventPackage);
    }

    // create new event package
    @PostMapping
    public ResponseEntity<Void> createEventPackage(@RequestBody EventPackagePayloadDTO packageDTO) {
        eventPackageService.createEventPackage(packageDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update event package
    @PutMapping("/{packageId}")
    public ResponseEntity<Void> updateEventPackage(
            @PathVariable Integer packageId,
            @RequestBody EventPackagePayloadDTO packageDTO) {
        eventPackageService.updateEventPackage(packageId, packageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete event package
    @DeleteMapping("/{packageId}")
    public ResponseEntity<Void> deleteEventPackage(@PathVariable Integer packageId) {
        eventPackageService.deleteEventPackage(packageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // restore event package
    @PutMapping("/{packageId}/restore")
    public ResponseEntity<Void> restoreEventPackage(@PathVariable Integer packageId) {
        eventPackageService.restoreEventPackage(packageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}