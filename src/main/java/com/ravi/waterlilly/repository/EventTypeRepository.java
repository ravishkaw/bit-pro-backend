package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of event type
@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
}
