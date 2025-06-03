package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// repository of inventory status
@Repository
public interface InventoryStatusRepository extends JpaRepository<InventoryStatus, Integer> {
    // get status by name
    @Query("SELECT s FROM InventoryStatus s WHERE s.name =?1")
    InventoryStatus findByName(String name);
}
