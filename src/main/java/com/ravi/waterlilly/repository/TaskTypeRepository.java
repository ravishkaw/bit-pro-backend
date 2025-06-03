package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of task type
@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Integer> {
}
