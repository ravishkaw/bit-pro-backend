package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// repository layer of task status
@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Integer> {

    // find task status by name
    @Query("SELECT s FROM TaskStatus s WHERE s.name=?1")
    TaskStatus findByName(String statusName);
}
