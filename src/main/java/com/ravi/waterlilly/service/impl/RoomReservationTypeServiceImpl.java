package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.RoomReservationType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.RoomReservationTypeRepository;
import com.ravi.waterlilly.service.RoomReservationTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of room reservation type Service.
@Service
@RequiredArgsConstructor
public class RoomReservationTypeServiceImpl implements RoomReservationTypeService {
    private final ModelMapper modelMapper;
    private final RoomReservationTypeRepository reservationTypeRepository;

    //Fetch all room reservation types
    @Override
    public List<ReferenceDataDTO> getAllRoomReservationTypes() {
        return reservationTypeRepository.findAll().stream()
                .map(reservationType -> modelMapper.map(reservationType, ReferenceDataDTO.class))
                .toList();
    }

    // get reservation type by id
    @Override
    public RoomReservationType getReservationTypeById(Integer id) {
        return reservationTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room Reservation Type", "id", id.toString()));
    }
}
