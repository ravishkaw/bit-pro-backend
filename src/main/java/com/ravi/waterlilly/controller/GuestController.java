package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.guest.GuestPayloadDTO;
import com.ravi.waterlilly.payload.guest.GuestPageResponse;
import com.ravi.waterlilly.payload.guest.GuestReferenceDTO;
import com.ravi.waterlilly.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handles all guest-related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/guests")
public class GuestController {
    private final GuestService service;

    //Fetches a paginated list of employees
    @GetMapping
    public ResponseEntity<GuestPageResponse> getAlls(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery

    ) {
        GuestPageResponse response = service.getAllGuests(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get all without pagination
    @GetMapping("/get-all")
    public ResponseEntity<List<GuestReferenceDTO>> getAllGuests() {
        List<GuestReferenceDTO> guests = service.getAllGuestsNoPagination();
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    //Gets details of a single guest.
    @GetMapping("/{guestId}")
    public ResponseEntity<GuestPayloadDTO> getAnGuest(@PathVariable Long guestId) {
        GuestPayloadDTO guestDTO = service.getAnGuest(guestId);
        return new ResponseEntity<>(guestDTO, HttpStatus.OK);
    }

    //Adds a new guest.
    @PostMapping
    public ResponseEntity<?> addGuest(@Valid @RequestBody GuestPayloadDTO guestPayloadDTO) {
        service.addGuest(guestPayloadDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Updates an existing guest.
    @PutMapping("/{guestId}")
    public ResponseEntity<?> updateGuest(@Valid @RequestBody GuestPayloadDTO guestDTO, @PathVariable Long guestId) {
        service.updateGuest(guestDTO, guestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Deletes a guest.
    @DeleteMapping("/{guestId}")
    public ResponseEntity<?> deleteGuest(@PathVariable Long guestId) {
        service.deleteGuest(guestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Restores a deleted guest.
    @PutMapping("/{guestId}/restore")
    public ResponseEntity<?> restoreGuest(@PathVariable Long guestId) {
        service.restoreGuest(guestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
