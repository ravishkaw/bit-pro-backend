package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.TaskTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of task target type
@Repository
public interface TaskTargetTypeRepository extends JpaRepository<TaskTargetType, Integer> {
}
