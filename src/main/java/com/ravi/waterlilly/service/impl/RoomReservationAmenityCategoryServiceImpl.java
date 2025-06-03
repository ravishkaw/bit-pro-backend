package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.RoomReservationAmenityCategory;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.RoomReservationAmenityCategoryRepository;
import com.ravi.waterlilly.service.RoomReservationAmenityCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of RoomReservationAmenityCategories service
@Service
@RequiredArgsConstructor
public class RoomReservationAmenityCategoryServiceImpl implements RoomReservationAmenityCategoryService {
    private final RoomReservationAmenityCategoryRepository roomReservationAmenityCategoryRepository;
    private final ModelMapper modelMapper;

    // get all categories list
    @Override
    public List<ReferenceDataDTO> getAllCategories() {
        return roomReservationAmenityCategoryRepository.findAll().stream()
                .map(categories -> modelMapper.map(categories, ReferenceDataDTO.class))
                .toList();
    }

    // get category by id
    @Override
    public RoomReservationAmenityCategory getCategoryById(Integer id) {
        return roomReservationAmenityCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room Reservation Amenity Category", "id", id.toString()));
    }
}
