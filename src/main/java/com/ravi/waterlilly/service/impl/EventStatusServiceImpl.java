package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.EventStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.EventStatusRepository;
import com.ravi.waterlilly.service.EventStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of event status service
@Service
@RequiredArgsConstructor
public class EventStatusServiceImpl implements EventStatusService {

    private final EventStatusRepository eventStatusRepository;
    private final ModelMapper modelMapper;

    // get all status
    @Override
    public List<ReferenceDataDTO> getAllStatus() {

        return eventStatusRepository.findAll().stream()
                .map(eventStatus -> modelMapper.map(eventStatus, ReferenceDataDTO.class))
                .toList();
    }

    // get event status by id
    @Override
    public EventStatus getEventStatusById(Integer id) {
        return eventStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Status", "id", id.toString()));
    }
}
