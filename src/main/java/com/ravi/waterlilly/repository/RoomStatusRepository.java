package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomStatusRepository extends JpaRepository<RoomStatus, Integer> {
    
    // get room status by name
    @Query("SELECT rs FROM RoomStatus rs WHERE rs.name=?1")
    RoomStatus getRoomStatusByName(String statusName);
}
