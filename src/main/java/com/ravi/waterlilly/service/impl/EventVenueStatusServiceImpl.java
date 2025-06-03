package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.EventVenue;
import com.ravi.waterlilly.model.EventVenueStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.EventVenueRepository;
import com.ravi.waterlilly.repository.EventVenueStatusRepository;
import com.ravi.waterlilly.service.EventVenueStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

// Implementation of EventVenueStatusService
@Service
@RequiredArgsConstructor
public class EventVenueStatusServiceImpl implements EventVenueStatusService {

    private final EventVenueRepository eventVenueRepository;
    private final EventVenueStatusRepository eventVenueStatusRepository;
    private final ModelMapper modelMapper;

    // Status constants
    private static final String STATUS_AVAILABLE = "Available";
    private static final String STATUS_RESERVED = "Reserved";
    private static final String STATUS_ONGOING_EVENT = "Ongoing Event";
    private static final String STATUS_NEEDS_CLEANING = "Needs Cleaning";
    private static final String STATUS_CLOSED = "Closed";
    private static final String STATUS_CLEANING_AND_MAINTENANCE = "Cleaning and Under Maintenance";

    // Fetch all event venue statuses as DTOs
    @Override
    public List<ReferenceDataDTO> getAllStatus() {
        return eventVenueStatusRepository.findAll().stream()
                .map(status -> modelMapper.map(status, ReferenceDataDTO.class))
                .toList();
    }

    // get event venue status by name
    @Override
    public EventVenueStatus getEventVenueStatusByName(String name) {
        EventVenueStatus status = eventVenueStatusRepository.findStatusByName(name);
        if (status == null) {
            throw new ResourceNotFoundException("Event Venue Status", "name", name);
        }
        return status;
    }

    // Update event venue status based on task status and type
    @Override
    public void updateStatusBasedOnTask(Long venueId, String taskStatus, String taskType) {
        EventVenue eventVenue = fetchEventVenue(venueId);
        String currentStatus = getStatusName(eventVenue);

        if (STATUS_NEEDS_CLEANING.equalsIgnoreCase(currentStatus)) {
            return;
        }

        if ("In Progress".equalsIgnoreCase(taskStatus)) {
            handleInProgressStatus(eventVenue, taskType, currentStatus);
        } else if ("Completed".equalsIgnoreCase(taskStatus) && isEventVenueAvailableForStatusUpdate(currentStatus)) {
            setEventVenueStatus(eventVenue, STATUS_AVAILABLE);
        }

        eventVenueRepository.save(eventVenue);
    }

    // Update venue status based on maintenance progress
    @Override
    public void updateStatusBasedOnMaintenance(Long venueId, String maintenanceStatusName) {
        EventVenue eventVenue = fetchEventVenue(venueId);
        String currentStatus = getStatusName(eventVenue);

        if (Set.of(STATUS_ONGOING_EVENT, STATUS_RESERVED).contains(currentStatus)) {
            return;
        }

        if ("In Progress".equalsIgnoreCase(maintenanceStatusName)) {
            setEventVenueStatus(eventVenue, STATUS_CLEANING_AND_MAINTENANCE);
        } else if ("Completed".equalsIgnoreCase(maintenanceStatusName) && isEventVenueAvailableForStatusUpdate(currentStatus)) {
            setEventVenueStatus(eventVenue, STATUS_AVAILABLE);
        }

        eventVenueRepository.save(eventVenue);
    }
    
    // Set venue status to "Available"
    @Override
    public void setVenueToAvailable(Long venueId) {
        EventVenue eventVenue = fetchEventVenue(venueId);
        String currentStatus = getStatusName(eventVenue);

        if (isEventVenueAvailableForStatusUpdate(currentStatus)) {
            setEventVenueStatus(eventVenue, STATUS_AVAILABLE);
            eventVenueRepository.save(eventVenue);
        }
    }

    // Set venue status to "Closed"
    @Override
    public void setVenueToClosed(Long venueId) {
        EventVenue eventVenue = fetchEventVenue(venueId);
        setEventVenueStatus(eventVenue, STATUS_CLOSED);
        eventVenueRepository.save(eventVenue);
    }

    // Map task type to in-progress status
    private void handleInProgressStatus(EventVenue eventVenue, String taskType, String currentStatus) {
        if (isEventVenueRestrictedForInProgress(currentStatus)) {
            return;
        }

        Map<String, String> taskTypeToStatusMap = Map.of(
                "Housekeeping", STATUS_CLEANING_AND_MAINTENANCE,
                "Maintenance", STATUS_CLEANING_AND_MAINTENANCE
        );

        String newStatus = taskTypeToStatusMap.get(taskType);
        if (newStatus != null) {
            setEventVenueStatus(eventVenue, newStatus);
        }
    }

    // Check if current status blocks "In Progress" updates
    private boolean isEventVenueRestrictedForInProgress(String currentStatus) {
        return Set.of(STATUS_ONGOING_EVENT, STATUS_CLOSED).contains(currentStatus);
    }

    // Check if venue can be made available
    private boolean isEventVenueAvailableForStatusUpdate(String currentStatus) {
        return !Set.of(STATUS_ONGOING_EVENT, STATUS_CLOSED, STATUS_RESERVED).contains(currentStatus);
    }

    // Set status using string name
    private void setEventVenueStatus(EventVenue eventVenue, String statusName) {
        EventVenueStatus status = getEventVenueStatusByName(statusName);
        if (status == null) {
            throw new APIException("Invalid status: " + statusName);
        }
        eventVenue.setStatus(status);
    }

    // Fetch event venue by ID
    private EventVenue fetchEventVenue(Long venueId) {
        return eventVenueRepository.findById(venueId)
                .orElseThrow(() -> new APIException("Invalid event venue id: " + venueId));
    }

    // Safely get current status name
    private String getStatusName(EventVenue eventVenue) {
        return eventVenue.getStatus() != null ? eventVenue.getStatus().getName() : "";
    }
}
