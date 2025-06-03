package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.InventoryItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository of inventory item type
@Repository
public interface InventoryItemTypeRepository extends JpaRepository<InventoryItemType, Integer> {
}
