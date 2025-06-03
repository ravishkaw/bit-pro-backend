package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.EventVenue;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.payload.eventVenue.EventVenuePageResponse;
import com.ravi.waterlilly.payload.eventVenue.EventVenuePayloadDTO;
import com.ravi.waterlilly.payload.eventVenue.EventVenueReferenceDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

// Service interface of Event Venue-related services.
public interface EventVenueService {

    // get all venues
    EventVenuePageResponse getAllVenues(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get available venues
    List<EventVenueReferenceDTO> getAvailableVenues(LocalDate startDate, LocalDate endDate, Integer capacity);

    // get venue by id
    EventVenue getVenueById(Long venueId);

    // get all venues  ( Id and name only)
    List<ReferenceDataDTO> getAllVenuesNoPagination();

    // get single venue
    EventVenuePayloadDTO getSingleVenue(Long venueId);

    // create venue
    void createVenue(EventVenuePayloadDTO eventVenuePayloadDTO) throws IOException;

    // update venue
    void updateVenue(Long venueId, EventVenuePayloadDTO eventVenuePayloadDTO) throws IOException;

    // delete venue
    void deleteVenue(Long venueId);

    // restore venue
    void restoreVenue(Long id);

}
