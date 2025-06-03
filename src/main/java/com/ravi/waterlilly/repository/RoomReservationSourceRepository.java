package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomReservationSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of room reservation source
@Repository
public interface RoomReservationSourceRepository extends JpaRepository<RoomReservationSource, Integer> {
}
