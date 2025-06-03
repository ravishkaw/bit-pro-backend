package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomReservation;
import com.ravi.waterlilly.model.RoomReservationStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// service layer of RoomReservationStatus
public interface RoomReservationStatusService {

    // get all room reservation status
    List<ReferenceDataDTO> getAllRoomReservationStatus();

    // get reservation status by id
    RoomReservationStatus getStatusById(Integer id);

    // helper methods for get confirmed status
    RoomReservationStatus getConfirmedStatus();

    // helper methods for get checked in status
    RoomReservationStatus getCheckedInStatus();

    // helper methods for get cancelled status
    RoomReservationStatus getCancelledStatus();

    // helper method to get pending status
    RoomReservationStatus getPendingStatus();

    // update room reservation status to an action
    void updateRoomReservationToAction(RoomReservation reservation, String action);

    // get status by name
    RoomReservationStatus getStatusByName(String statusName);
}
