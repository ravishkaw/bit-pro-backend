package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of event status
@Repository
public interface EventStatusRepository extends JpaRepository<EventStatus, Integer> {
}
