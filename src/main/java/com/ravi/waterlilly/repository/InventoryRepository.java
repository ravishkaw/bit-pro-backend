package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Inventory;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// repository of inventory
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    //search for inventory items in db
    @Query("SELECT it from Inventory  it WHERE " +
            "LOWER(it.itemName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(it.itemType.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<Inventory> searchInventoryItems(@Param("searchQuery") String searchQuery, Pageable pageable);

    // find item by name
    @Query("SELECT i FROM Inventory i WHERE i.itemName=?1")
    Inventory findByItemName(String itemName);
}
