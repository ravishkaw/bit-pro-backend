package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Inventory;
import com.ravi.waterlilly.payload.inventory.InventoryPayloadDTO;
import com.ravi.waterlilly.payload.inventory.InventoryPageResponse;
import com.ravi.waterlilly.payload.inventory.InventoryQuantityDTO;

import java.util.List;

//  Service interface for managing inventory.
public interface InventoryService {

    // get all inventory items with pagination
    InventoryPageResponse getAllInventoryItems(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get all items
    List<InventoryPayloadDTO> getAll();

    // get single item
    InventoryPayloadDTO getInventoryItem(Long id);

    // Add item
    void addInventoryItem(InventoryPayloadDTO inventoryPayloadDTO);

    // update item
    void updateInventoryItem(InventoryPayloadDTO inventoryPayloadDTO, Long id);

    // delete item
    void deleteInventoryItem(Long id);

    // restore item
    void restoreInventoryItem(Long id);

    // get inventory by id
    Inventory getInventoryById(Long id);

    // validate item availability
    void validateInventoryAvailability(List<InventoryQuantityDTO> inventoryItems);

    // inventory update for a room
    void reserveInventory(List<InventoryQuantityDTO> inventoryItems);

    // Release inventory from room (when room is updated/deleted)
    void releaseInventory(List<InventoryQuantityDTO> inventoryItems);

    // Decrease inventory quantity
    void decreaseInventoryQuantity(Long inventoryId, Integer quantityToDecrease);

    // Increase inventory quantity
    void increaseInventoryQuantity(Long inventoryId, Integer quantityToIncrease);
}
