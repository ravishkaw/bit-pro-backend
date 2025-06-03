package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.EventVenueStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// service layer of event venue status
public interface EventVenueStatusService {

    // Fetch all event venue statuses as DTOs
    List<ReferenceDataDTO> getAllStatus();

    // get event venue status by name
    EventVenueStatus getEventVenueStatusByName(String name);

    // Update event venue status based on task status and type
    void updateStatusBasedOnTask(Long venueId, String taskStatus, String taskType);

    // Update venue status based on maintenance progress
    void updateStatusBasedOnMaintenance(Long venueId, String maintenanceStatusName);

    // Set venue status to "Available"
    void setVenueToAvailable(Long venueId);

    // Set venue status to "Closed"
    void setVenueToClosed(Long venueId);
}
