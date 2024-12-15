package com.ravi.waterlilly.features.guest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/")
    public String greeting() {
        return "hello";
    }

    @GetMapping("/guests")
    public ResponseEntity<GuestResponse> getAllGuests() {
        GuestResponse guestResponses = guestService.getAllGuests();
        return new ResponseEntity<>(guestResponses, HttpStatus.OK);
    }

    @PostMapping("/guest")
    public ResponseEntity<String> addGuest(@Valid @RequestBody GuestDTO guestDTO) {
        guestService.addGuest(guestDTO);
        return new ResponseEntity<>("Guest Added", HttpStatus.CREATED);
    }

    @PutMapping("/guest/{id}")
    public ResponseEntity<GuestDTO> updateGuest(@PathVariable Long id, @RequestBody GuestDTO guestDTO) {
        GuestDTO savedGuestDTO = guestService.updateGuest(id, guestDTO);
        return new ResponseEntity<>(savedGuestDTO, HttpStatus.OK);
    }

    @DeleteMapping("/guest/{id}")
    public ResponseEntity<String> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return new ResponseEntity<>("Guest Deleted", HttpStatus.OK);
    }
}
