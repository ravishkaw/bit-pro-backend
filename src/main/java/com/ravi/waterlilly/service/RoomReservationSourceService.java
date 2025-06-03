package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomReservationSource;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing room reservation sources
public interface RoomReservationSourceService {

    //Fetches a list of room reservation sources
    List<ReferenceDataDTO> getAllRoomReservationSources();

    // get source by id
    RoomReservationSource getSourceById(Integer id);
}
