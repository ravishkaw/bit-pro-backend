package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Room;
import com.ravi.waterlilly.payload.room.RoomCardForReservationDTO;
import com.ravi.waterlilly.payload.room.RoomPayloadDTO;
import com.ravi.waterlilly.payload.room.RoomCardDTO;
import com.ravi.waterlilly.payload.room.RoomReferenceDTO;
import org.springframework.data.domain.Slice;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// service layer of rooms
public interface RoomService {

    // get all rooms
    List<RoomCardDTO> getAllRooms();

    // get all with id and number
    List<RoomReferenceDTO> getAll();

    // filter rooms
    List<RoomCardDTO> filterRooms(Integer roomTypeId, Integer statusId, BigDecimal minPrice, BigDecimal maxPrice, Integer adults, Integer children, Integer infants, String searchQuery);

    // get a single room details
    RoomPayloadDTO getSingleRoom(Long roomId);

    // get available rooms for a stay between two dates with adults, children and infants
    List<RoomCardForReservationDTO> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, Integer adults, Integer children, Integer infants);

    // add a new room
    void addNewRoom(RoomPayloadDTO room) throws IOException;

    // update a room
    void updateRoom(RoomPayloadDTO roomPayloadDTO, Long roomId) throws IOException;

    // delete a room
    void deleteRoom(Long roomId);

    // restore deleted room
    void restoreRoom(Long roomId);

    // method to fetch room by id
    Room getRoomById(Long targetId);

    // calculate total room price with pricing rules for the stay
    BigDecimal calculateReservationTotalPrice(Room room, LocalDateTime checkInDate, LocalDateTime checkOutDate);

    // validate room availability when reserving
    void validateRoomAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut);
}
