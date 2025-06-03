package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// repository layer of room reservation
@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {

    // get reservation to a status
    @Query("SELECT r FROM RoomReservation r WHERE r.roomReservationStatus.name = :status")
    Page<RoomReservation> getReservationByStatus(@Param("status") String status, Pageable pageable);

    // search reservation
    @Query("SELECT DISTINCT r FROM RoomReservation r " +
            "LEFT JOIN r.guests g " +
            "LEFT JOIN r.room rm " +
            "WHERE LOWER(g.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) " +
            "OR LOWER(g.callingName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) " +
            "OR LOWER(g.email) LIKE LOWER(CONCAT('%', :searchQuery, '%')) " +
            "OR LOWER(g.mobileNo) LIKE LOWER(CONCAT('%', :searchQuery, '%')) " +
            "OR LOWER(g.idNumber) LIKE LOWER(CONCAT('%', :searchQuery, '%')) " +
            "OR CAST(rm.number AS string) LIKE CONCAT('%', :searchQuery, '%') " +
            "OR LOWER(rm.roomType.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) " +
            "OR LOWER(r.note) LIKE LOWER(CONCAT('%', :searchQuery, '%')) " +
            "OR CAST(r.reservedCheckInDate AS string) LIKE CONCAT('%', :searchQuery, '%') " +
            "OR CAST(r.reservedCheckOutDate AS string) LIKE CONCAT('%', :searchQuery, '%')")
    Page<RoomReservation> searchReservation(@Param("searchQuery") String searchQuery, Pageable pageable);


    // check the room available or not when adding a reservation excluding the current reservation
    @Query("SELECT r FROM RoomReservation r " +
            "WHERE r.room.id = :roomId " +
            "AND r.id != :excludeReservationId " +
            "AND (" +
            "    (r.reservedCheckInDate <= :checkOut AND r.reservedCheckOutDate >= :checkIn) " +
            "    OR " +
            "    (r.checkInDate IS NOT NULL AND r.checkOutDate IS NOT NULL AND " +
            "     r.checkInDate <= :checkOut AND " +
            "     r.checkOutDate >= :checkIn)" +
            ") " +
            "AND r.roomReservationStatus.name NOT IN ('Cancelled', 'No-Show', 'Checked-Out')")
    List<RoomReservation> findOverlappingReservationsExcludingCurrent(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("excludeReservationId") Long excludeReservationId
    );

    // Find reservations of a status on a date
    @Query("SELECT r FROM RoomReservation r WHERE r.roomReservationStatus.name = :status AND r.reservedCheckInDate < :date")
    List<RoomReservation> findByStatusAndDate(@Param("status") String status, @Param("date") LocalDateTime date);
}
