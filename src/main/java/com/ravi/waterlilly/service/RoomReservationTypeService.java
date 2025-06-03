package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomReservationType;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing room reservation type service.
public interface RoomReservationTypeService {

    //Fetches a list of Room status.
    List<ReferenceDataDTO> getAllRoomReservationTypes();

    // get reservation type by id
    RoomReservationType getReservationTypeById(Integer id);
}
