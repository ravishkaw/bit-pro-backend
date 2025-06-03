
package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.EventService;
import com.ravi.waterlilly.model.Status;
import com.ravi.waterlilly.payload.eventService.EventServicePageResponse;
import com.ravi.waterlilly.payload.eventService.EventServicePayloadDTO;
import com.ravi.waterlilly.repository.EventServiceRepository;
import com.ravi.waterlilly.service.EventServicesService;
import com.ravi.waterlilly.service.StatusService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

// Implementation of EventServicesService.
@Service
@RequiredArgsConstructor
public class EventServicesServiceImpl implements EventServicesService {
    private final EventServiceRepository eventServiceRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final StatusService statusService;

    // get all services
    @Override
    public EventServicePageResponse getAllEventServices(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {
        // privilege check
        privilegeUtils.privilegeCheck("Event Reservation Service", AppConstants.SELECT);

        // Determine sorting order
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create Pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<EventService> eventServicePage = StringUtils.hasText(searchQuery)
                ? eventServiceRepository.searchEventServices(searchQuery.trim(), pageable)
                : eventServiceRepository.findAll(pageable);

        // Extract list and map to DTOs
        List<EventServicePayloadDTO> dto = eventServicePage.getContent().stream()
                .map(service -> modelMapper.map(service, EventServicePayloadDTO.class))
                .toList();

        // Create response
        return new EventServicePageResponse(
                dto,
                eventServicePage.getNumber(),
                eventServicePage.getSize(),
                eventServicePage.getTotalElements(),
                eventServicePage.getTotalPages(),
                eventServicePage.isLast()
        );
    }

    // get all services without pagination
    @Override
    public List<EventServicePayloadDTO> getAllEventServicesNoPagination() {

        // check privileges
        privilegeUtils.privilegeCheck("Event Reservation Service", AppConstants.SELECT);

        return eventServiceRepository.findAll().stream()
                .map(events -> modelMapper.map(events, EventServicePayloadDTO.class))
                .toList();

    }

    // get single service.
    @Override
    public EventServicePayloadDTO getSingleService(Integer id) {
        // privilege check
        privilegeUtils.privilegeCheck("Event Reservation Service", AppConstants.SELECT);

        EventService eventService = getEventServiceById(id);

        return modelMapper.map(eventService, EventServicePayloadDTO.class);
    }

    // add new service
    @Override
    public void addEventService(EventServicePayloadDTO eventServicePayloadDTO) {
        // privilege check
        privilegeUtils.privilegeCheck("Event Reservation Service", AppConstants.INSERT);

        // validate uniqueness
        validateEventServiceUniqueness(eventServicePayloadDTO, null);

        // create and save
        EventService eventService = new EventService();
        updateEventServiceFields(eventServicePayloadDTO, eventService);
        eventServiceRepository.save(eventService);
    }

    // update an existing service
    @Override
    public void updateEventService(EventServicePayloadDTO eventServicePayloadDTO, Integer id) {
        // privilege check
        privilegeUtils.privilegeCheck("Event Reservation Service", AppConstants.UPDATE);

        // get existing service
        EventService existingService = getEventServiceById(id);

        // validate uniqueness
        validateEventServiceUniqueness(eventServicePayloadDTO, id);

        // update and save
        updateEventServiceFields(eventServicePayloadDTO, existingService);
        eventServiceRepository.save(existingService);
    }

    // delete an existing service
    @Override
    public void deleteEventService(Integer id) {
        // privilege check
        privilegeUtils.privilegeCheck("Event Reservation Service", AppConstants.DELETE);

        // get existing service
        EventService existingService = getEventServiceById(id);

        // update status and save
        Status status = statusService.getDeletedStatus();
        existingService.setStatus(status);
        eventServiceRepository.save(existingService);
    }

    // restore an existing service
    @Override
    public void restoreEventService(Integer id) {

        // privilege check
        privilegeUtils.privilegeCheck("Event Reservation Service", AppConstants.UPDATE);

        // get existing service
        EventService existingService = getEventServiceById(id);

        // update status and save
        Status status = statusService.getActiveStatus();
        existingService.setStatus(status);
        eventServiceRepository.save(existingService);
    }

    // get service by id
    @Override
    public EventService getEventServiceById(Integer id) {
        return eventServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Reservation Service", "id", String.valueOf(id)));
    }

    // Helper Method to update event service fields
    private void updateEventServiceFields(EventServicePayloadDTO dto, EventService service) {
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPricePerUnit(dto.getPricePerUnit());
        service.setStatus(statusService.getStatusByName(dto.getStatusName()));
    }

    // validate uniqueness of event service
    private void validateEventServiceUniqueness(EventServicePayloadDTO dto, Integer id) {
        EventService duplicateService = eventServiceRepository.findByName(dto.getName());
        if (duplicateService != null && !duplicateService.getId().equals(id)) {
            throw new APIException("Event Reservation Service with name " + dto.getName() + " already exists");
        }
    }
}