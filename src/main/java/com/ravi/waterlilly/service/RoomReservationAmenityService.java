package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomReservationAmenity;
import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityQuantityDTO;
import com.ravi.waterlilly.payload.roomReservationAmenity.RoomReservationAmenityPageResponse;
import com.ravi.waterlilly.payload.roomReservationAmenity.RoomReservationAmenityPayloadDTO;
import com.ravi.waterlilly.payload.roomReservationAmenity.RoomReservationAmenityTableData;

import java.math.BigDecimal;
import java.util.List;

// service layer of room reservation amenity
public interface RoomReservationAmenityService {

    // get all
    RoomReservationAmenityPageResponse getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get all without pagination
    List<RoomReservationAmenityTableData> getRoomReservationAmenities();

    //get single amenity
    RoomReservationAmenityPayloadDTO getSingleAmenity(Integer amenityId);

    // get amenity by id
    RoomReservationAmenity getAmenityById(Integer amenityId);

    // Add new amenity
    void addRoomReservationAmenity(RoomReservationAmenityPayloadDTO amenityPayloadDTO);

    //void update an amenity
    void updateRoomReservationAmenity(RoomReservationAmenityPayloadDTO payloadDTO, Integer amenityId);

    // delete an amenity
    void deleteRoomReservationAmenity(Integer amenityId);

    // restore an amenity
    void restoreRoomReservationAmenity(Integer amenityId);

    // Helper method to calculate amenity price
    BigDecimal calculateAmenityPrice(List<AmenityQuantityDTO> amenities);
}
