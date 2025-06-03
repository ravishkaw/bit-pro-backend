package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// repository of room
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // filter rooms
    @Query("SELECT r FROM Room r WHERE " +
            "(:roomTypeId IS NULL OR r.roomType.id = :roomTypeId) AND " +
            "(:statusId IS NULL OR r.status.id = :statusId) AND " +
            "(:searchQuery IS NULL OR CAST(r.number AS string) LIKE %:searchQuery% OR LOWER(r.description) LIKE %:searchQuery%) AND " +
            "(:minPrice IS NULL OR r.roomType.basePrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR r.roomType.basePrice <= :maxPrice) AND " +
            "(:adults IS NULL OR r.adultNo >= :adults) AND " +
            "(:children IS NULL OR r.childNo >= :children) AND " +
            "(:infants IS NULL OR r.infantNo >= :infants)")
    List<Room> findRoomsWithFilters(
            @Param("roomTypeId") Integer roomTypeId,
            @Param("statusId") Integer statusId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("adults") Integer adults,
            @Param("children") Integer children,
            @Param("infants") Integer infants,
            @Param("searchQuery") String searchQuery
    );

    // find room by number
    @Query("SELECT r FROM Room r WHERE r.number =?1")
    Room findByNumber(Integer number);

    // get available rooms for a stay between two dates with adults, children and infants
    @Query("SELECT r FROM Room r " +
            "WHERE r.id NOT IN (" +
            "    SELECT rr.room.id FROM RoomReservation rr " +
            "    WHERE (" +
            "        (rr.reservedCheckInDate <= :checkOutDateTime AND rr.reservedCheckOutDate >= :checkInDateTime) " +
            "        OR " +
            "        (rr.checkInDate IS NOT NULL AND rr.checkOutDate IS NOT NULL AND " +
            "         rr.checkInDate <= :checkOutDateTime AND " +
            "         rr.checkOutDate >= :checkInDateTime)" +
            "    ) " +
            "    AND rr.roomReservationStatus.name NOT IN ('Cancelled', 'No-Show', 'Checked-Out')" +
            ") " +
            "AND r.adultNo >= :adults " +
            "AND (r.childNo >= :children OR :children = 0) " +
            "AND (r.infantNo >= :infants OR :infants = 0) " +
            "AND (CAST(:checkInDateTime AS date) > CURRENT_DATE AND r.status.name NOT IN ('Out of Service','Under Maintenance')" +
            "    OR " +
            "    (CAST(:checkInDateTime AS date) = CURRENT_DATE AND r.status.name = 'Available')" +
            ")")
    List<Room> findAvailableRooms(
            @Param("checkInDateTime") LocalDateTime checkInDateTime,
            @Param("checkOutDateTime") LocalDateTime checkOutDateTime,
            @Param("adults") Integer adults,
            @Param("children") Integer children,
            @Param("infants") Integer infants
    );

    // check if the room as active or upcoming reservations on next 7 days
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM RoomReservation r " +
            "WHERE r.room.id = :roomId " +
            "AND ((r.reservedCheckInDate BETWEEN :startDate AND :endDate) " +
            "OR (r.reservedCheckOutDate BETWEEN :startDate AND :endDate))")
    boolean existsByRoomIdAndDateRange(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}