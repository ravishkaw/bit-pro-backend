package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Guest;
import com.ravi.waterlilly.payload.guest.GuestPayloadDTO;
import com.ravi.waterlilly.payload.guest.GuestPageResponse;
import com.ravi.waterlilly.payload.guest.GuestReferenceDTO;

import java.util.List;

// Service interface for managing guests.
public interface GuestService {

    //Retrieves a paginated list of guests.
    GuestPageResponse getAllGuests(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // Get all guests without pagination
    List<GuestReferenceDTO> getAllGuestsNoPagination();

    //Gets details of a single guest.
    GuestPayloadDTO getAnGuest(Long guestId);

    // Adds a new guest.
    void addGuest(GuestPayloadDTO guestDTO);

    //Updates an existing guest.
    void updateGuest(GuestPayloadDTO guestDTO, Long guestId);

    //Deletes a guest.
    void deleteGuest(Long guestId);

    // restore a guest
    void restoreGuest(Long guestId);

    // get guest by id
    Guest getGuestById(Long guestId);
}
