package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.EventStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// service layer of the event status
public interface EventStatusService {

    // get all event status
    List<ReferenceDataDTO> getAllStatus();

    // get event status by id
    EventStatus getEventStatusById(Integer id);
}
