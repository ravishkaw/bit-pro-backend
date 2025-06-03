package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomInventoryRepository extends JpaRepository<RoomInventory, Integer> {

    // room inventory items
    @Query("SELECT ri FROM RoomInventory ri WHERE " +
            "LOWER(ri.inventory.itemName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(ri.inventory.itemType.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<RoomInventory> searchRoomInventoryItems(@Param("searchQuery") String searchQuery, Pageable pageable);

    // find inventory item for a room
    @Query("SELECT ri FROM RoomInventory ri WHERE ri.room.id=?2 AND ri.inventory.id=?1")
    RoomInventory findByInventoryNameAndRoom(Long inventoryId, Long roomId);

    // get quantity of a item of a room
    @Query("SELECT ri.quantity FROM RoomInventory ri WHERE ri.inventory.id=?1 AND ri.room.id=?1")
    Integer getQuantityOfItemInARoom(Integer roomId, Integer inventoryId);
}
