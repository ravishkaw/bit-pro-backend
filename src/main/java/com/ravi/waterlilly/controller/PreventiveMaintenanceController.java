package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.preventiveMaintenance.PreventiveMaintenancePageResponse;
import com.ravi.waterlilly.payload.preventiveMaintenance.PreventiveMaintenancePayloadDTO;
import com.ravi.waterlilly.service.PreventiveMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// handle all preventive maintenance related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/preventive-maintenance")
public class PreventiveMaintenanceController {
    private final PreventiveMaintenanceService preventiveMaintenanceService;

    // get all maintenance records
    @GetMapping
    public ResponseEntity<PreventiveMaintenancePageResponse> getMaintenances(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery
    ) {
        PreventiveMaintenancePageResponse maintenances = preventiveMaintenanceService.getMaintenances(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(maintenances, HttpStatus.OK);
    }

    // get single maintenance records
    @GetMapping("/{id}")
    public ResponseEntity<PreventiveMaintenancePayloadDTO> getSingleMaintenance(@PathVariable Integer id) {
        PreventiveMaintenancePayloadDTO maintenancePayloadDTO = preventiveMaintenanceService.getSingleMaintenance(id);
        return new ResponseEntity<>(maintenancePayloadDTO, HttpStatus.OK);
    }

    // add new record
    @PostMapping
    public ResponseEntity<PreventiveMaintenancePayloadDTO> addNewMaintenance(@RequestBody PreventiveMaintenancePayloadDTO maintenancePayloadDTO) {
        preventiveMaintenanceService.addNewMaintenance(maintenancePayloadDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update a record
    @PutMapping("/{id}")
    public ResponseEntity<PreventiveMaintenancePayloadDTO> updateMaintenance(@RequestBody PreventiveMaintenancePayloadDTO maintenancePayloadDTO, @PathVariable Integer id) {
        preventiveMaintenanceService.updateMaintenance(maintenancePayloadDTO, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a record
    @DeleteMapping("/{id}")
    public ResponseEntity<PreventiveMaintenancePayloadDTO> deleteMaintenance(@PathVariable Integer id) {
        preventiveMaintenanceService.deleteMaintenance(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
