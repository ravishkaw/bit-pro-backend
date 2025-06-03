package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.eventService.EventServicePageResponse;
import com.ravi.waterlilly.payload.eventService.EventServicePayloadDTO;
import com.ravi.waterlilly.service.EventServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// handle all event service related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/event-services")
public class EventServiceController {
    private final EventServicesService eventServicesService;

    // get all event services
    @GetMapping
    public ResponseEntity<EventServicePageResponse> getAllEventServices(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery
    ) {
        EventServicePageResponse response = eventServicesService.getAllEventServices(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get all services without pagination
    @GetMapping("/get-all")
    public ResponseEntity<List<EventServicePayloadDTO>> getAllEventServicesNoPagination() {
        List<EventServicePayloadDTO> response = eventServicesService.getAllEventServicesNoPagination();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get single service
    @GetMapping("/{id}")
    public ResponseEntity<EventServicePayloadDTO> getSingleService(@PathVariable Integer id) {
        EventServicePayloadDTO service = eventServicesService.getSingleService(id);
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    // add new service
    @PostMapping
    public ResponseEntity<?> addNewService(@RequestBody EventServicePayloadDTO serviceDTO) {
        eventServicesService.addEventService(serviceDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update an existing service
    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@RequestBody EventServicePayloadDTO serviceDTO, @PathVariable Integer id) {
        eventServicesService.updateEventService(serviceDTO, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete an existing service
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Integer id) {
        eventServicesService.deleteEventService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // restore a deleted service
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreService(@PathVariable Integer id) {
        eventServicesService.restoreEventService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
