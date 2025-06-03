package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.RoomReservationSource;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.RoomReservationSourceRepository;
import com.ravi.waterlilly.service.RoomReservationSourceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of room reservation source service
@Service
@RequiredArgsConstructor
public class RoomReservationSourceServiceImpl implements RoomReservationSourceService {

    private final RoomReservationSourceRepository roomReservationSourceRepository;
    private final ModelMapper modelMapper;

    // fetch all sources
    @Override
    public List<ReferenceDataDTO> getAllRoomReservationSources() {
        return roomReservationSourceRepository.findAll().stream()
                .map(sources -> modelMapper.map(sources, ReferenceDataDTO.class))
                .toList();
    }

    // get source by id
    @Override
    public RoomReservationSource getSourceById(Integer id) {
        return roomReservationSourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation Source", "id", id.toString()));
    }
}
