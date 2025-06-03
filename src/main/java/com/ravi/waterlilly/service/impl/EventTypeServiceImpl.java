package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.EventType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.EventTypeRepository;
import com.ravi.waterlilly.service.EventTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of EventType Service.
@Service
@RequiredArgsConstructor
public class EventTypeServiceImpl implements EventTypeService {
    private final EventTypeRepository eventTypeRepository;
    private final ModelMapper modelMapper;

    // get all event types
    @Override
    public List<ReferenceDataDTO> getAllEventTypes() {
        return eventTypeRepository.findAll().stream()
                .map(eventType -> modelMapper.map(eventType, ReferenceDataDTO.class))
                .toList();
    }

    // get event type by id
    @Override
    public EventType getEventTypeById(Integer id) {
        return eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Type", "id", id.toString()));
    }
}
