package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.TimeUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of time unit
@Repository
public interface TimeUnitRepository extends JpaRepository<TimeUnit, Integer> {
}
