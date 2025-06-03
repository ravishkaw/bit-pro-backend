package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomReservationAmenityCategory;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// service layer of RoomReservationAmenityCategories
public interface RoomReservationAmenityCategoryService {

    // get all room reservation amenity categories
    List<ReferenceDataDTO> getAllCategories();

    // get category by id
    RoomReservationAmenityCategory getCategoryById(Integer id);
}
