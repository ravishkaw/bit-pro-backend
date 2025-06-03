package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.InventoryItemTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle inventory item type api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/inventory-item-types")
public class InventoryItemTypeController {
    private final InventoryItemTypeService inventoryItemTypeService;

    // get all
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAll() {
        List<ReferenceDataDTO> list = inventoryItemTypeService.getAllTypes();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
