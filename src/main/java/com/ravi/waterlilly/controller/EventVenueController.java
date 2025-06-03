package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.payload.eventVenue.EventVenuePageResponse;
import com.ravi.waterlilly.payload.eventVenue.EventVenuePayloadDTO;
import com.ravi.waterlilly.payload.eventVenue.EventVenueReferenceDTO;
import com.ravi.waterlilly.service.EventVenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

// handle event venue related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/event-venues")
public class EventVenueController {

    private final EventVenueService eventVenueService;

    // get all event venues
    @GetMapping
    public ResponseEntity<EventVenuePageResponse> getAllEventVenues(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery
    ) {
        EventVenuePageResponse response = eventVenueService.getAllVenues(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get available venues
    @GetMapping("/available-venues")
    public ResponseEntity<List<EventVenueReferenceDTO>> getAvailableVenues(
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate,
            @RequestParam(name = "capacity") Integer capacity
    ) {
        List<EventVenueReferenceDTO> venues = eventVenueService.getAvailableVenues(startDate, endDate, capacity);
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }

    // get all venues ( Id name)
    @GetMapping("/get-all")
    public ResponseEntity<List<ReferenceDataDTO>> getAllVenues() {
        List<ReferenceDataDTO> venues = eventVenueService.getAllVenuesNoPagination();
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }

    // get single event venue
    @GetMapping("/{id}")
    public ResponseEntity<EventVenuePayloadDTO> getSingleVenue(@PathVariable Long id) {
        EventVenuePayloadDTO venue = eventVenueService.getSingleVenue(id);
        return new ResponseEntity<>(venue, HttpStatus.OK);
    }

    // add new venue
    @PostMapping
    public ResponseEntity<?> addNewVenue(@RequestBody EventVenuePayloadDTO venuePayloadDTO) throws IOException {
        eventVenueService.createVenue(venuePayloadDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update existing venue
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVenue(@RequestBody EventVenuePayloadDTO venuePayloadDTO, @PathVariable Long id) throws IOException {
        eventVenueService.updateVenue(id, venuePayloadDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete existing venue
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVenue(@PathVariable Long id) {
        eventVenueService.deleteVenue(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // restore deleted venue
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreVenue(@PathVariable Long id) {
        eventVenueService.restoreVenue(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
