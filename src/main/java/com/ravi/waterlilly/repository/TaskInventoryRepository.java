package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.TaskInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of task inventory
@Repository
public interface TaskInventoryRepository extends JpaRepository<TaskInventory, Integer> {
}
