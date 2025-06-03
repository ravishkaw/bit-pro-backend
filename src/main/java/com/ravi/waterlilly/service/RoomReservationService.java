package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomReservation;
import com.ravi.waterlilly.payload.roomReservation.*;

// service layer of room reservation
public interface RoomReservationService {
    // get all reservations
    RoomReservationPageResponse getAllReservations(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String status, String searchQuery);

    // get a single reservation
    RoomReservationPayloadDTO getSingleReservation(Long reservationId);

    // add new reservation
    void addRoomReservation(RoomReservationPayloadFormDTO roomReservation);

    // update existing reservation
    void updateRoomReservation(Long reservationId, RoomReservationPayloadFormDTO roomReservation);

    // update reservation status to action
    void updateStatusToAction(Long reservationId, String action);

    // checkout
    String reservationCheckOut(Long reservationId, CheckOutDTO checkOutDTO);

    // method to get room reservation by id
    RoomReservation getRoomReservationById(Long reservationId);
}
