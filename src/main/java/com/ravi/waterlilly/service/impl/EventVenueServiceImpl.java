package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.EventVenue;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.payload.eventVenue.EventVenuePageResponse;
import com.ravi.waterlilly.payload.eventVenue.EventVenuePayloadDTO;
import com.ravi.waterlilly.payload.eventVenue.EventVenueReferenceDTO;
import com.ravi.waterlilly.repository.EventVenueRepository;
import com.ravi.waterlilly.service.EventVenueService;
import com.ravi.waterlilly.service.EventVenueStatusService;
import com.ravi.waterlilly.service.ImageService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Implementation of EventVenue Service.
@Service
@RequiredArgsConstructor
public class EventVenueServiceImpl implements EventVenueService {
    private final PrivilegeUtils privilegeUtils;
    private final EventVenueRepository eventVenueRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final EventVenueStatusService eventVenueStatusService;

    // get all venues
    @Override
    public EventVenuePageResponse getAllVenues(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Venue", AppConstants.SELECT);

        // sort and pagination
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);

        Page<EventVenue> eventVenuePage = StringUtils.hasText(searchQuery)
                ? eventVenueRepository.searchVenues(searchQuery.trim(), pageable)
                : eventVenueRepository.findAll(pageable);

        List<EventVenue> eventVenues = eventVenuePage.getContent();

        List<EventVenuePayloadDTO> tableData = eventVenues.stream()
                .map(eventVenue -> modelMapper.map(eventVenue, EventVenuePayloadDTO.class))
                .toList();

        return new EventVenuePageResponse(
                tableData,
                eventVenuePage.getNumber(),
                eventVenuePage.getSize(),
                eventVenuePage.getTotalElements(),
                eventVenuePage.getTotalPages(),
                eventVenuePage.isLast()
        );
    }

    // get available venues
    @Override
    public List<EventVenueReferenceDTO> getAvailableVenues(LocalDate startDate, LocalDate endDate, Integer capacity) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Venue", AppConstants.SELECT);

        LocalDateTime startDateTime = startDate.atTime(AppConstants.DEFAULT_EVENT_START_TIME);
        LocalDateTime endDateTime = endDate.atTime(AppConstants.DEFAULT_EVENT_END_TIME);

        return eventVenueRepository.findAvailableVenues(startDateTime, endDateTime, capacity).stream()
                .map(venue -> modelMapper.map(venue, EventVenueReferenceDTO.class))
                .toList();
    }

    // get all venues  ( Id and name only)
    @Override
    public List<ReferenceDataDTO> getAllVenuesNoPagination() {

        // check privileges
        privilegeUtils.privilegeCheck("Event Venue", AppConstants.SELECT);

        return eventVenueRepository.findAll().stream()
                .map(venue -> modelMapper.map(venue, ReferenceDataDTO.class))
                .toList();
    }


    // get single venue
    @Override
    public EventVenuePayloadDTO getSingleVenue(Long venueId) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Venue", AppConstants.SELECT);

        EventVenue venue = getVenueById(venueId);

        return modelMapper.map(venue, EventVenuePayloadDTO.class);
    }

    // create a new venue
    @Override
    public void createVenue(EventVenuePayloadDTO eventVenuePayloadDTO) throws IOException {

        // check privileges
        privilegeUtils.privilegeCheck("Event Venue", AppConstants.INSERT);

        // check uniqueness
        validateUniqueness(eventVenuePayloadDTO, null);

        EventVenue venue = new EventVenue();
        updateVenueFields(eventVenuePayloadDTO, venue);
        eventVenueRepository.save(venue);
    }

    // update an existing venue
    @Override
    public void updateVenue(Long venueId, EventVenuePayloadDTO eventVenuePayloadDTO) throws IOException {

        // check privileges
        privilegeUtils.privilegeCheck("Event Venue", AppConstants.UPDATE);

        EventVenue venue = getVenueById(venueId);

        // check uniqueness
        validateUniqueness(eventVenuePayloadDTO, venueId);

        // existing image
        String existingImage = venue.getPhoto();

        updateVenueFields(eventVenuePayloadDTO, venue);
        eventVenueRepository.save(venue);

        // delete image from server if image not matches
        if (existingImage != null && !existingImage.equals(venue.getPhoto())) {
            imageService.deleteImage("event venues", existingImage);
        }
    }

    // delete an existing venue ( soft delete )
    @Override
    public void deleteVenue(Long venueId) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Venue", AppConstants.DELETE);

        eventVenueStatusService.setVenueToClosed(venueId);
    }

    // restore a venue
    @Override
    public void restoreVenue(Long id) {

        // check privileges
        privilegeUtils.privilegeCheck("Event Venue", AppConstants.UPDATE);
        eventVenueStatusService.setVenueToAvailable(id);
    }

    // get venue by id
    @Override
    public EventVenue getVenueById(Long venueId) {
        return eventVenueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue", "venueId", venueId.toString()));
    }

    // Helper method to update venue fields
    private void updateVenueFields(EventVenuePayloadDTO venuePayloadDTO, EventVenue venue) {
        venue.setName(venuePayloadDTO.getName());
        venue.setCapacity(venuePayloadDTO.getCapacity());
        venue.setDescription(venuePayloadDTO.getDescription());
        venue.setPhoto(venuePayloadDTO.getPhoto());
        venue.setStatus(eventVenueStatusService.getEventVenueStatusByName(venuePayloadDTO.getStatusName()));
    }

    // Helper method to validate uniqueness of the venue
    private void validateUniqueness(EventVenuePayloadDTO venuePayloadDTO, Long venueId) throws IOException {

        // check name
        EventVenue existingVenue = eventVenueRepository.findByName(venuePayloadDTO.getName());

        if (existingVenue != null && !existingVenue.getId().equals(venueId)) {
            imageService.deleteImage("event venues", venuePayloadDTO.getPhoto());
            throw new APIException("Venue with name " + venuePayloadDTO.getName() + " already exists");
        }
    }
}
