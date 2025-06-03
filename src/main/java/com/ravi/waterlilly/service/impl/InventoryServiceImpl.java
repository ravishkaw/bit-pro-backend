package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Inventory;
import com.ravi.waterlilly.payload.inventory.InventoryPayloadDTO;
import com.ravi.waterlilly.payload.inventory.InventoryPageResponse;
import com.ravi.waterlilly.payload.inventory.InventoryQuantityDTO;
import com.ravi.waterlilly.payload.inventory.InventoryTableDataDTO;
import com.ravi.waterlilly.repository.InventoryRepository;
import com.ravi.waterlilly.service.InventoryItemTypeService;
import com.ravi.waterlilly.service.InventoryService;
import com.ravi.waterlilly.service.InventoryStatusService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

// implementation of inventory service
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final PrivilegeUtils privilegeUtils;
    private final ModelMapper modelMapper;
    private final InventoryStatusService inventoryStatusService;
    private final InventoryItemTypeService inventoryItemTypeService;

    // get all inventory items with pagination
    @Override
    public InventoryPageResponse getAllInventoryItems(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        //check privileges
        privilegeUtils.privilegeCheck("Inventory", AppConstants.SELECT);

        // Determine the sorting order and sort by
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create a Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<Inventory> page = StringUtils.hasText(searchQuery)
                ? inventoryRepository.searchInventoryItems(searchQuery, pageable)
                : inventoryRepository.findAll(pageable);

        // Extract the list of users from the page
        List<Inventory> inventories = page.getContent();

        // Map the list of Users to a list of usersTableDTOS using ModelMapper
        List<InventoryTableDataDTO> inventoryPayloadDTOS = inventories.stream()
                .map(inventory -> modelMapper.map(inventory, InventoryTableDataDTO.class))
                .toList();

        // Create a response object of users data
        return new InventoryPageResponse(
                inventoryPayloadDTOS,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // get all items
    @Override
    public List<InventoryPayloadDTO> getAll() {

        // check privileges
        privilegeUtils.privilegeCheck("Inventory", AppConstants.SELECT);

        return inventoryRepository.findAll().stream()
                .map(items -> modelMapper.map(items, InventoryPayloadDTO.class))
                .toList();
    }

    // get single item
    @Override
    public InventoryPayloadDTO getInventoryItem(Long inventoryId) {
        //check privileges
        privilegeUtils.privilegeCheck("Inventory", AppConstants.SELECT);

        Inventory inventory = getInventoryById(inventoryId);

        return modelMapper.map(inventory, InventoryPayloadDTO.class);
    }

    // add new item
    @Override
    public void addInventoryItem(InventoryPayloadDTO inventoryPayloadDTO) {
        //check privileges
        privilegeUtils.privilegeCheck("Inventory", AppConstants.INSERT);

        // check item is in db
        validateInventoryItemUniqueness(inventoryPayloadDTO, null);

        Inventory newInventory = new Inventory();
        updateInventoryFields(inventoryPayloadDTO, newInventory);
        inventoryRepository.save(newInventory);
    }

    // update existing item
    @Override
    public void updateInventoryItem(InventoryPayloadDTO inventoryPayloadDTO, Long inventoryId) {
        //check privileges
        privilegeUtils.privilegeCheck("Inventory", AppConstants.UPDATE);

        // check item is in db
        validateInventoryItemUniqueness(inventoryPayloadDTO, inventoryId);

        // get existing inventory
        Inventory existingInventory = getInventoryById(inventoryId);

        updateInventoryFields(inventoryPayloadDTO, existingInventory);
        inventoryRepository.save(existingInventory);
    }

    // delete item
    @Override
    public void deleteInventoryItem(Long inventoryId) {
        //check privileges
        privilegeUtils.privilegeCheck("Inventory", AppConstants.DELETE);
        inventoryStatusService.setInventoryToOutOfStock(inventoryId);
    }

    // restore a item
    @Override
    public void restoreInventoryItem(Long inventoryId) {
        //check privileges
        privilegeUtils.privilegeCheck("Inventory", AppConstants.UPDATE);
        inventoryStatusService.setInventoryToInStock(inventoryId);
    }

    // Helper method to fetch inventory
    @Override
    public Inventory getInventoryById(Long inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "inventory Id", inventoryId));
    }

    // validate inventory item availability
    @Override
    public void validateInventoryAvailability(List<InventoryQuantityDTO> inventoryItems) {
        if (inventoryItems == null || inventoryItems.isEmpty()) {
            return;
        }

        for (InventoryQuantityDTO item : inventoryItems) {
            Inventory inventory = getInventoryById(item.getInventoryId());

            // Check if item is in stock
            if (!"In Stock".equals(inventory.getStatus().getName())) {
                throw new APIException("Inventory item '" + inventory.getItemName() + "' is not in stock");
            }

            // Check if sufficient quantity is available
            if (inventory.getQuantity() < item.getQuantity()) {
                throw new APIException("Insufficient quantity for '" + inventory.getItemName() +
                        "'. Available: " + inventory.getQuantity() + ", Required: " + item.getQuantity());
            }
        }
    }

    // Reserve inventory for room setup
    @Override
    public void reserveInventory(List<InventoryQuantityDTO> inventoryItems) {
        if (inventoryItems == null || inventoryItems.isEmpty()) {
            return;
        }

        // First validate all items are available
        validateInventoryAvailability(inventoryItems);

        // Then decrease quantities
        for (InventoryQuantityDTO item : inventoryItems) {
            decreaseInventoryQuantity(item.getInventoryId(), item.getQuantity());
        }
    }

    // Release inventory from room (when room is updated/deleted)
    @Override
    public void releaseInventory(List<InventoryQuantityDTO> inventoryItems) {
        if (inventoryItems == null || inventoryItems.isEmpty()) {
            return;
        }

        // Increase quantities back to available stock
        for (InventoryQuantityDTO item : inventoryItems) {
            increaseInventoryQuantity(item.getInventoryId(), item.getQuantity());
        }
    }

    // Decrease inventory quantity
    @Override
    public void decreaseInventoryQuantity(Long inventoryId, Integer quantityToDecrease) {
        if (quantityToDecrease <= 0) {
            throw new APIException("Quantity to decrease must be greater than 0");
        }

        Inventory inventory = getInventoryById(inventoryId);

        if (inventory.getQuantity() < quantityToDecrease) {
            throw new APIException("Cannot decrease quantity. Available: " + inventory.getQuantity() +
                    ", Requested decrease: " + quantityToDecrease);
        }

        Integer newQuantity = inventory.getQuantity() - quantityToDecrease;
        inventory.setQuantity(newQuantity);

        // Update status if out of stock
        if (newQuantity == 0) {
            inventoryStatusService.setInventoryToOutOfStock(inventoryId);
        }

        inventory.setLastModifiedDatetime(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }

    // Increase inventory quantity
    @Override
    public void increaseInventoryQuantity(Long inventoryId, Integer quantityToIncrease) {
        if (quantityToIncrease <= 0) {
            throw new APIException("Quantity to increase must be greater than 0");
        }

        Inventory inventory = getInventoryById(inventoryId);
        Integer newQuantity = inventory.getQuantity() + quantityToIncrease;
        inventory.setQuantity(newQuantity);

        // Update status to in stock if was out of stock
        if ("Out of Stock".equals(inventory.getStatus().getName()) && newQuantity > 0) {
            inventoryStatusService.setInventoryToInStock(inventoryId);
        }

        inventory.setLastModifiedDatetime(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }

    // Helper method to update inventory fields
    private void updateInventoryFields(InventoryPayloadDTO inventoryPayloadDTO, Inventory inventory) {
        // validate quantity is not 0 or less
        validateQuantity(inventoryPayloadDTO.getQuantity());
        inventory.setItemName(inventoryPayloadDTO.getItemName());
        inventory.setQuantity(inventoryPayloadDTO.getQuantity());
        inventory.setPrice(inventoryPayloadDTO.getPrice());
        inventory.setLastRestockedDate(inventoryPayloadDTO.getLastRestockedDate());
        inventory.setItemType(inventoryItemTypeService.getInventoryItemTypeById(inventoryPayloadDTO.getItemTypeId()));
        inventory.setStatus(inventoryStatusService.getInventoryStatusById(inventoryPayloadDTO.getStatusId()));

        LocalDateTime now = LocalDateTime.now();
        if (inventory.getId() == null) {
            inventory.setAddedDatetime(now);
        }
        inventory.setLastModifiedDatetime(now);
    }

    // Helper method to check if the employee is in db
    private void validateInventoryItemUniqueness(InventoryPayloadDTO inventoryPayloadDTO, Long inventoryId) {
        //check existing item
        Inventory duplicateInventory = inventoryRepository.findByItemName(inventoryPayloadDTO.getItemName());

        if (duplicateInventory != null && !duplicateInventory.getId().equals(inventoryId))
            throw new APIException("An Inventory Item with " + duplicateInventory.getItemName() + " already exists!");
    }

    // Helper method to validate quantity is not 0 or less
    private void validateQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new APIException("Quantity must be greater than 0");
        }
    }
}
