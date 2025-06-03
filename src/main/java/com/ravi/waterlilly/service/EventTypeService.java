package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.EventType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// service layer of event type
public interface EventTypeService {

    // get all event types
    List<ReferenceDataDTO> getAllEventTypes();

    // get event type by id
    EventType getEventTypeById(Integer id);
}
