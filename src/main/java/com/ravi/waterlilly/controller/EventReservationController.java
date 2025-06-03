package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.eventReservation.EventReservationPageResponse;
import com.ravi.waterlilly.payload.eventReservation.EventReservationPayloadDTO;
import com.ravi.waterlilly.payload.eventReservation.EventReservationPayloadFormDTO;
import com.ravi.waterlilly.service.EventReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// handle event-reservation-related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/event-reservations")
public class EventReservationController {

    private final EventReservationService eventReservationService;

    // get paginated reservations
    @GetMapping
    public ResponseEntity<EventReservationPageResponse> getAllEventReservations(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery,
            @RequestParam(name = "status", defaultValue = "ONGOING") String status
    ) {
        EventReservationPageResponse response = eventReservationService.getAllReservations(
                pageNumber, pageSize, sortBy, sortOrder, status, searchQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get single reservation
    @GetMapping("/{reservationId}")
    public ResponseEntity<EventReservationPayloadDTO> getSingleReservation(@PathVariable Long reservationId) {
        EventReservationPayloadDTO reservation = eventReservationService.getSingleReservation(reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    // add new reservation
    @PostMapping
    public ResponseEntity<String> addEventReservation(@RequestBody EventReservationPayloadFormDTO formDTO) {
        eventReservationService.addEventReservation(formDTO);
        return new ResponseEntity<>("Event reservation added successfully", HttpStatus.CREATED);
    }

    // update existing reservation
    @PutMapping("/{reservationId}")
    public ResponseEntity<String> updateEventReservation(
            @PathVariable Long reservationId,
            @RequestBody EventReservationPayloadFormDTO formDTO) {
        eventReservationService.updateEventReservation(reservationId, formDTO);
        return new ResponseEntity<>("Event reservation updated successfully", HttpStatus.OK);
    }
}