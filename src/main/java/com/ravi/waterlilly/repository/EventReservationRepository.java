package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EventReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// repository layer of event reservation
@Repository
public interface EventReservationRepository extends JpaRepository<EventReservation, Long> {

    // get event reservation by status
    @Query("SELECT er FROM EventReservation er WHERE er.eventStatus.name = :status")
    Page<EventReservation> getReservationByEventStatus(@Param("status") String status, Pageable pageable);

    // search event reservations
    @Query("SELECT er FROM EventReservation er")
    Page<EventReservation> searchEventReservations(@Param("searchQuery") String searchQuery, Pageable pageable);

    // check if there are any other reservations on the time period for the venue
    @Query("SELECT er FROM EventReservation er WHERE " +
            "er.eventVenue.id = :venueId AND " +
            "er.startDatetime <= :endDatetime AND " +
            "er.endDatetime >= :startDatetime AND " +
            "er.id != :reservationId AND " +
            "er.eventStatus.name NOT IN ('COMPLETED', 'CANCELLED')")
    List<EventReservation> findOverlappingReservationsExcludingCurrent(
            @Param("venueId") Long venueId,
            @Param("startDateTime") LocalDateTime startDatetime,
            @Param("endDateTune") LocalDateTime endDatetime,
            @Param("reservationId") Long reservationId);
}
