package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EventVenueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// repository layer of event venue status
@Repository
public interface EventVenueStatusRepository extends JpaRepository<EventVenueStatus, Integer> {
    // find status by name
    @Query("SELECT s FROM EventVenueStatus s WHERE s.name=?1")
    EventVenueStatus findStatusByName(String statusName);
}
