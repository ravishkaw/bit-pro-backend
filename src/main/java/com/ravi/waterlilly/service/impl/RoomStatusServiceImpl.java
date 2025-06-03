package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Room;
import com.ravi.waterlilly.model.RoomStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.RoomRepository;
import com.ravi.waterlilly.repository.RoomStatusRepository;
import com.ravi.waterlilly.service.RoomStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomStatusServiceImpl implements RoomStatusService {

    private final ModelMapper modelMapper;
    private final RoomStatusRepository roomStatusRepository;
    private final RoomRepository roomRepository;

    // Constants for room status names
    private static final String STATUS_AVAILABLE = "Available";
    private static final String STATUS_NEEDS_CLEANING = "Needs Cleaning";
    private static final String STATUS_OCCUPIED = "Occupied";
    private static final String STATUS_OUT_OF_SERVICE = "Out of Service";
    private static final String STATUS_RESERVED = "Reserved";
    private static final String STATUS_CLEANING_IN_PROGRESS = "Cleaning in Progress";
    private static final String STATUS_UNDER_MAINTENANCE = "Under Maintenance";

    // Get all room statuses as DTOs
    @Override
    public List<ReferenceDataDTO> getAllStatus() {
        return roomStatusRepository.findAll().stream()
                .map(status -> modelMapper.map(status, ReferenceDataDTO.class))
                .toList();
    }

    // Get a single room status by its ID
    @Override
    public RoomStatus getStatusById(Integer statusId) {
        return roomStatusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Room status", "id", statusId.toString()));
    }

    // Set the room status to "Needs Cleaning"
    @Override
    public void setRoomToNeedsCleaning(Long roomId) {
        Room room = fetchRoom(roomId);
        setRoomStatus(room, STATUS_NEEDS_CLEANING);
        roomRepository.save(room);
    }

    // Set the room status to "Occupied"
    @Override
    public void setRoomToOccupied(Long roomId) {
        Room room = fetchRoom(roomId);
        setRoomStatus(room, STATUS_OCCUPIED);
        roomRepository.save(room);
    }

    // Set the room status to "Available" if allowed
    @Override
    public void setRoomToAvailable(Long roomId) {
        Room room = fetchRoom(roomId);
        String currentStatus = getRoomStatusName(room);

        if (isRoomAvailableForStatusUpdate(currentStatus)) {
            setRoomStatus(room, STATUS_AVAILABLE);
            roomRepository.save(room);
        }
    }

    // Set the room status to "Out of Service" after checking for reservations
    @Override
    public void setRoomToOutOfService(Long roomId) {
        Room room = fetchRoom(roomId);
        validateRoomStatusChangeAllowed(room, STATUS_OUT_OF_SERVICE);
        setRoomStatus(room, STATUS_OUT_OF_SERVICE);
        roomRepository.save(room);
    }

    // Change the room status using status ID, with validation
    @Override
    public void validateStatusChangeForReservations(Room room, Integer statusId) {
        RoomStatus newStatus = getStatusById(statusId);
        if (room.getStatus() == null || newStatus.getId().equals(room.getStatus().getId())) {
            return;
        }
        validateRoomStatusChangeAllowed(room, newStatus.getName());
        setRoomStatus(room, newStatus.getName());
        roomRepository.save(room);
    }

    // update room status base on maintenance
    @Override
    public void updateStatusBasedOnMaintenance(Long roomId, String maintenanceStatusName) {
        Room room = fetchRoom(roomId);
        String currentStatus = getRoomStatusName(room);

        // Skip if room is already occupied or reserved
        if (Set.of(STATUS_OCCUPIED, STATUS_RESERVED).contains(currentStatus)) {
            return;
        }
        if ("In Progress".equalsIgnoreCase(maintenanceStatusName)) {
            setRoomStatus(room, STATUS_UNDER_MAINTENANCE);
        } else if ("Completed".equalsIgnoreCase(maintenanceStatusName) && isRoomAvailableForStatusUpdate(currentStatus)) {
            setRoomStatus(room, STATUS_AVAILABLE);
        }
        roomRepository.save(room);
    }

    // Update room status based on task progress and type
    @Override
    public void updateStatusBasedOnTask(Long roomId, String taskStatus, String taskType) {
        Room room = fetchRoom(roomId);
        String currentStatus = getRoomStatusName(room);

        if (STATUS_NEEDS_CLEANING.equalsIgnoreCase(currentStatus)) {
            return;
        }

        if ("In Progress".equalsIgnoreCase(taskStatus)) {
            handleInProgressStatus(room, taskType, currentStatus);
        } else if ("Completed".equalsIgnoreCase(taskStatus) && isRoomAvailableForStatusUpdate(currentStatus)) {
            setRoomStatus(room, STATUS_AVAILABLE);
        }
        roomRepository.save(room);
    }

    // Check if status change is allowed based on upcoming reservations
    private void validateRoomStatusChangeAllowed(Room room, String newStatus) {
        LocalDateTime now = LocalDateTime.now();
        boolean hasActiveReservations = roomRepository.existsByRoomIdAndDateRange(
                room.getId(),
                now,
                now.plusDays(3)
        );

        if (hasActiveReservations) {
            throw new APIException("Cannot update room status to " + newStatus +
                    ". Room has active or upcoming reservations in next 3 days.");
        }
    }

    // Set room status to an "In Progress" state based on task type
    private void handleInProgressStatus(Room room, String taskType, String currentStatus) {
        if (isRestrictedForInProgress(currentStatus)) {
            return;
        }

        Map<String, String> taskTypeToStatusMap = Map.of(
                "Housekeeping", STATUS_CLEANING_IN_PROGRESS,
                "Maintenance", STATUS_UNDER_MAINTENANCE
        );

        String newStatus = taskTypeToStatusMap.get(taskType);
        if (newStatus != null) {
            setRoomStatus(room, newStatus);
        }
    }

    // Check if current status is not allowed to switch to "In Progress"
    private boolean isRestrictedForInProgress(String currentStatus) {
        return Set.of(STATUS_OCCUPIED, STATUS_OUT_OF_SERVICE).contains(currentStatus);
    }

    // Check if room can be marked as "Available"
    private boolean isRoomAvailableForStatusUpdate(String currentStatus) {
        return !Set.of(STATUS_OCCUPIED, STATUS_OUT_OF_SERVICE, STATUS_RESERVED).contains(currentStatus);
    }

    // Set room status using the name
    private void setRoomStatus(Room room, String statusName) {
        RoomStatus status = roomStatusRepository.getRoomStatusByName(statusName);
        if (status == null) {
            throw new ResourceNotFoundException("Room Status", "name", statusName);
        }
        room.setStatus(status);
        room.setLastModifiedDatetime(LocalDateTime.now());
    }

    // Get room from by id
    private Room fetchRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
    }

    // Get the name of the current room status safely
    private String getRoomStatusName(Room room) {
        return room.getStatus() != null ? room.getStatus().getName() : "";
    }
}
