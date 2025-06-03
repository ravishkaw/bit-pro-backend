package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Inventory;
import com.ravi.waterlilly.model.InventoryStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.InventoryRepository;
import com.ravi.waterlilly.repository.InventoryStatusRepository;
import com.ravi.waterlilly.service.InventoryStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// implementation of inventory status service
@Service
@RequiredArgsConstructor
public class InventoryStatusServiceImpl implements InventoryStatusService {
    private final InventoryStatusRepository inventoryStatusRepository;
    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;

    private final String INVENTORY_STATUS_IN_STOCK = "In Stock";
    private final String INVENTORY_STATUS_OUT_OF_STOCK = "Out of Stock";

    // get all status
    @Override
    public List<ReferenceDataDTO> findAll() {
        return inventoryStatusRepository.findAll().stream()
                .map(status -> modelMapper.map(status, ReferenceDataDTO.class))
                .toList();
    }

    // get inventory status by id
    @Override
    public InventoryStatus getInventoryStatusById(Integer statusId) {
        return inventoryStatusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory status", "id", statusId.toString()));
    }

    // Helper method to set inventory to in stock
    @Override
    public void setInventoryToInStock(Long inventoryId) {
        Inventory inventory = fetchInventory(inventoryId);
        setStatus(inventory, INVENTORY_STATUS_IN_STOCK);
        inventoryRepository.save(inventory);
    }

    // Helper method to set inventory out of stock
    @Override
    public void setInventoryToOutOfStock(Long inventoryId) {
        Inventory inventory = fetchInventory(inventoryId);
        setStatus(inventory, INVENTORY_STATUS_OUT_OF_STOCK);
        inventoryRepository.save(inventory);
    }

    // Helper method to set inventory status
    private void setStatus(Inventory inventory, String statusName) {
        InventoryStatus inventoryStatus = inventoryStatusRepository.findByName(statusName);
        inventory.setStatus(inventoryStatus);
        inventory.setLastModifiedDatetime(LocalDateTime.now());
    }

    // Helper method to fetch inventory
    private Inventory fetchInventory(Long inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", inventoryId));
    }
}
