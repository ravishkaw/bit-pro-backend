package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.TaskCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of task cost
@Repository
public interface TaskCostRepository extends JpaRepository<TaskCost, Long> {
}
