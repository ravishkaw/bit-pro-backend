package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.RoomFacility;
import com.ravi.waterlilly.payload.roomFacility.RoomFacilityPayloadDTO;
import com.ravi.waterlilly.payload.roomFacility.RoomFacilityTableResponse;
import jakarta.validation.Valid;

import java.util.List;

// Service interface of room facility service
public interface RoomFacilityService {

    // get all facilities with pagination
    RoomFacilityTableResponse getAllFacilities(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get all facilities
    List<RoomFacilityPayloadDTO> getFacilities();

    // get single facility
    RoomFacilityPayloadDTO getSingleFacility(Integer facilityId);

    // add new facility
    void addNewFacility(@Valid RoomFacilityPayloadDTO roomFacilityDTO);

    // update a facility
    void updateFacility(@Valid RoomFacilityPayloadDTO roomFacilityDTO, Integer facilityId);

    // delete a facility
    void deleteFacility(Integer facilityId);

    //restore a deleted facility
    void restoreRoomFacility(Integer facilityId);

    // get room facility by id
    RoomFacility getRoomFacilityById(Integer facilityId);
}