package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EventVenue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// repository layer of event venue
@Repository
public interface EventVenueRepository extends JpaRepository<EventVenue, Long> {

    // search venues
    @Query("SELECT ev FROM EventVenue ev WHERE " +
            "LOWER(ev.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(ev.description) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<EventVenue> searchVenues(@Param("searchQuery") String searchQuery, Pageable pageable);

    // find venue by name
    @Query("SELECT ev FROM EventVenue ev WHERE ev.name=?1")
    EventVenue findByName(String name);

    // get all venues for a time period
    @Query("SELECT v FROM EventVenue v WHERE v.id NOT IN (" +
            " SELECT r.eventVenue.id FROM EventReservation r " +
            " WHERE (r.startDatetime <= :endTime AND r.endDatetime >= :startTime) " +
            " AND r.eventStatus.name NOT IN ('COMPLETED', 'CANCELLED')) " +
            "AND v.status.name = 'Active' " +
            "AND v.capacity >= :capacity")
    List<EventVenue> findAvailableVenues(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("capacity") Integer capacity
    );

}
