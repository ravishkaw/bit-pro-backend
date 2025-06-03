package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.InventoryStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle inventory status related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/inventory-status")
public class InventoryStatusController {
    private final InventoryStatusService inventoryStatusService;

    // fetch all
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllInventoryStatuses() {
        List<ReferenceDataDTO> inventoryStatuses = inventoryStatusService.findAll();
        return new ResponseEntity<>(inventoryStatuses, HttpStatus.OK);
    }
}
