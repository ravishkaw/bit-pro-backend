package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// repository of RoomReservationStatus
@Repository
public interface RoomReservationStatusRepository extends JpaRepository<RoomReservationStatus, Integer> {

    // find status by name
    @Query("SELECT rs FROM RoomReservationStatus rs WHERE rs.name=?1")
    RoomReservationStatus findByName(String statusName);
}
