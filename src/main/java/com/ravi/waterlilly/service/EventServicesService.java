package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.EventService;
import com.ravi.waterlilly.payload.eventService.EventServicePageResponse;
import com.ravi.waterlilly.payload.eventService.EventServicePayloadDTO;

import java.util.List;

// Service interface of Event Service
public interface EventServicesService {

    // get all event services
    EventServicePageResponse getAllEventServices(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get all event services without pagination
    List<EventServicePayloadDTO> getAllEventServicesNoPagination();

    // get single service.
    EventServicePayloadDTO getSingleService(Integer id);

    // add event service
    void addEventService(EventServicePayloadDTO eventServicePayloadDTO);

    // update event service
    void updateEventService(EventServicePayloadDTO eventServicePayloadDTO, Integer id);

    // delete event service
    void deleteEventService(Integer id);

    // restore event service
    void restoreEventService(Integer id);

    // get service by id
    EventService getEventServiceById(Integer id);
}
