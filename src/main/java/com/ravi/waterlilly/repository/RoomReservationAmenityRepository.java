package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomReservationAmenity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// repository of RoomReservationAmenities
@Repository
public interface RoomReservationAmenityRepository extends JpaRepository<RoomReservationAmenity, Integer> {
    // get amenity by name
    @Query("SELECT a FROM RoomReservationAmenity a WHERE a.name=?1")
    RoomReservationAmenity findByName(String name);

    // search room amenity
    @Query("SELECT a FROM RoomReservationAmenity a WHERE " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(a.category.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<RoomReservationAmenity> searchReservationAmenity(@Param("searchQuery") String searchQuery, Pageable pageable);
}
