package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Room;
import com.ravi.waterlilly.model.RoomStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing room status service.
public interface RoomStatusService {

    //Fetches a list of Room status.
    List<ReferenceDataDTO> getAllStatus();

    // get status by id
    RoomStatus getStatusById(Integer statusId);

    // update room status base on maintenance
    void updateStatusBasedOnMaintenance(Long roomId, String maintenanceStatusName);

    // Update room status based on task progress and type
    void updateStatusBasedOnTask(Long roomId, String taskStatus, String taskType);

    // Set the room status to "Needs Cleaning"
    void setRoomToNeedsCleaning(Long roomId);

    // Set the room status to "Occupied"
    void setRoomToOccupied(Long roomId);

    // Set the room status to "Available" if allowed
    void setRoomToAvailable(Long roomId);

    // Set the room status to "Out of Service" after checking for reservations
    void setRoomToOutOfService(Long roomId);

    // Validates if room status can be changed considering active reservations
    void validateStatusChangeForReservations(Room room, Integer statusId);
}
