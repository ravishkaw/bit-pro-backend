package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomType;
import com.ravi.waterlilly.payload.roomType.RoomTypeBasicDTO;
import com.ravi.waterlilly.payload.roomType.RoomTypePayloadDTO;
import com.ravi.waterlilly.payload.roomType.RoomTypePageResponse;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//  Service interface for managing room types.
public interface RoomTypeService {

    // get all room types
    RoomTypePageResponse getAllRoomTypes(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get all room types without pagination
    List<RoomTypeBasicDTO> getRoomTypes();

    // get single room type
    RoomTypePayloadDTO getSingleRoomType(Integer roomTypeId);

    // add new room type
    void addNewRoomType(@Valid RoomTypePayloadDTO roomTypeDTO);

    // update a room type
    void updateRoomType(@Valid RoomTypePayloadDTO roomTypeDTO, Integer roomTypeId);

    // delete a room type
    void deleteRoomType(Integer roomTypeId);

    // restore deleted room type
    void restoreRoomType(Integer roomTypeId);

    // method to fetch room type by id
    RoomType fetchRoomTypeById(Integer roomTypeId);

    // calculate room type price based on pricing rules
    BigDecimal calculateRoomTypePriceForReservation(RoomType roomType, LocalDateTime reservedCheckInDate, LocalDateTime reservedCheckOutDate);
}
