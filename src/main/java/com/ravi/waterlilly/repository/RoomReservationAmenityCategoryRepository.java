package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomReservationAmenityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository of RoomReservationAmenityCategories
@Repository
public interface RoomReservationAmenityCategoryRepository extends JpaRepository<RoomReservationAmenityCategory, Integer> {
}
