package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.inventory.InventoryPayloadDTO;
import com.ravi.waterlilly.payload.inventory.InventoryPageResponse;
import com.ravi.waterlilly.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handles all inventory-related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    // get inventory items with pagination
    @GetMapping
    public ResponseEntity<InventoryPageResponse> getAllInventoryItems(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery) {
        InventoryPageResponse response = inventoryService.getAllInventoryItems(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get all inventory items
    @GetMapping("/get-all")
    public ResponseEntity<List<InventoryPayloadDTO>> getAll() {
        List<InventoryPayloadDTO> basicDTO = inventoryService.getAll();
        return new ResponseEntity<>(basicDTO, HttpStatus.OK);
    }

    // get single item
    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryPayloadDTO> getSingleInventoryItem(@PathVariable Long inventoryId) {
        InventoryPayloadDTO item = inventoryService.getInventoryItem(inventoryId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    // add new inventory
    @PostMapping
    public ResponseEntity<?> addInventoryItem(@RequestBody InventoryPayloadDTO inventoryDTO) {
        inventoryService.addInventoryItem(inventoryDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // update inventory
    @PutMapping("/{inventoryId}")
    public ResponseEntity<?> updateInventoryItem(@PathVariable Long inventoryId, @RequestBody InventoryPayloadDTO inventoryDTO) {
        inventoryService.updateInventoryItem(inventoryDTO, inventoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete inventory
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<?> deleteInventoryItem(@PathVariable Long inventoryId) {
        inventoryService.deleteInventoryItem(inventoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // restore inventory
    @PutMapping("/{inventoryId}/restore")
    public ResponseEntity<?> restoreInventoryItem(@PathVariable Long inventoryId) {
        inventoryService.restoreInventoryItem(inventoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
