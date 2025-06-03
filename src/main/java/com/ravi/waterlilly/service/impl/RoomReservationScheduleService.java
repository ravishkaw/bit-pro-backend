package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.model.RoomReservation;
import com.ravi.waterlilly.model.RoomReservationStatus;
import com.ravi.waterlilly.repository.RoomReservationRepository;
import com.ravi.waterlilly.repository.RoomReservationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomReservationScheduleService {

    private final RoomReservationRepository roomReservationRepository;
    private final RoomReservationStatusRepository roomReservationStatusRepository;

    // Scheduled tasks for updating reservation statuses
    @Scheduled(cron = "0 0 * * * *")
    private void updateOverdueReservationStatuses() {
        LocalDateTime now = LocalDateTime.now();

        // Find reservations that are past their check-in time and still in CONFIRMED status
        List<RoomReservation> overdueReservations = roomReservationRepository.findByStatusAndDate("CONFIRMED", now);

        if (!overdueReservations.isEmpty()) {
            RoomReservationStatus noShowStatus = roomReservationStatusRepository.findByName("NO-SHOW");

            if (noShowStatus == null) {
                throw new APIException("NO_SHOW status not found");
            }

            for (RoomReservation reservation : overdueReservations) {
                reservation.setRoomReservationStatus(noShowStatus);
                reservation.setLastModifiedDatetime(now);
            }

            roomReservationRepository.saveAll(overdueReservations);
        }
    }
}
