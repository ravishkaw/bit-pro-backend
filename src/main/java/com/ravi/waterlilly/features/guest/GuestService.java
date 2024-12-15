package com.ravi.waterlilly.features.guest;

public interface GuestService {
    GuestResponse getAllGuests();

    void addGuest(GuestDTO guestDTO);

    GuestDTO updateGuest(Long id, GuestDTO guestDTO);

    void deleteGuest(Long id);
}
