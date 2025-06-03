package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomReservationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// room reservation type repository
@Repository
public interface RoomReservationTypeRepository extends JpaRepository<RoomReservationType, Integer> {
}
