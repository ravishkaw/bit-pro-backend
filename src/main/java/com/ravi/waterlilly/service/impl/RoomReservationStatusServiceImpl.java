package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.RoomReservation;
import com.ravi.waterlilly.model.RoomReservationStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.RoomReservationRepository;
import com.ravi.waterlilly.repository.RoomReservationStatusRepository;
import com.ravi.waterlilly.service.RoomReservationStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of RoomReservationStatus service
@Service
@RequiredArgsConstructor
public class RoomReservationStatusServiceImpl implements RoomReservationStatusService {
    private final RoomReservationStatusRepository roomReservationStatusRepository;
    private final ModelMapper modelMapper;
    private final RoomReservationRepository roomReservationRepository;

    // get all room reservation status
    @Override
    public List<ReferenceDataDTO> getAllRoomReservationStatus() {
        return roomReservationStatusRepository.findAll().stream()
                .map(status -> modelMapper.map(status, ReferenceDataDTO.class))
                .toList();
    }

    // get reservation status by id
    @Override
    public RoomReservationStatus getStatusById(Integer id) {
        return roomReservationStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation Status", "id", id.toString()));
    }

    // helper methods for get confirmed status
    @Override
    public RoomReservationStatus getConfirmedStatus() {
        return roomReservationStatusRepository.findByName("CONFIRMED");
    }

    // helper methods for get checked in status
    @Override
    public RoomReservationStatus getCheckedInStatus() {
        return roomReservationStatusRepository.findByName("CHECKED-IN");
    }

    // helper methods for get cancelled status
    @Override
    public RoomReservationStatus getCancelledStatus() {
        return roomReservationStatusRepository.findByName("CANCELLED");
    }

    // helper method to get pending status
    @Override
    public RoomReservationStatus getPendingStatus() {
        return roomReservationStatusRepository.findByName("PENDING");
    }

    // update room reservation status to an action
    @Override
    public void updateRoomReservationToAction(RoomReservation reservation, String action) {
        RoomReservationStatus status = switch (action) {
            case "cancel" -> getCancelledStatus();
            case "check-in" -> getCheckedInStatus();
            case "confirm" -> getConfirmedStatus();
            default -> throw new APIException("Invalid action: " + action);
        };
        reservation.setRoomReservationStatus(status);
        roomReservationRepository.save(reservation);
    }

    // get status by name
    @Override
    public RoomReservationStatus getStatusByName(String statusName) {
        RoomReservationStatus status = roomReservationStatusRepository.findByName(statusName);
        if (status == null) {
            throw new ResourceNotFoundException("Reservation Status", "name", statusName);
        }
        return status;
    }
}
