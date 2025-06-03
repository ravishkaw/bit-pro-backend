package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomFacility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// repository of room facility
@Repository
public interface RoomFacilityRepository extends JpaRepository<RoomFacility, Integer> {

    // Get all active facilities
    @Query("SELECT ra FROM RoomFacility ra WHERE ra.status.name = 'Active'")
    List<RoomFacility> getActiveFacilities();

    //Search amenities
    @Query("SELECT ra FROM RoomFacility ra WHERE " +
            "LOWER(ra.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(ra.description) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<RoomFacility> searchFacilities(@Param("searchQuery") String searchQuery, Pageable pageable);

    // Find amenity by name
    @Query("SELECT ra FROM RoomFacility ra WHERE ra.name=?1")
    RoomFacility findByFacilityName(String name);
}
