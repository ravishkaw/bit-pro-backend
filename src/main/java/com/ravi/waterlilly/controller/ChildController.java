package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.child.ChildPageResponse;
import com.ravi.waterlilly.payload.child.ChildPayloadDTO;
import com.ravi.waterlilly.service.ChildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/children")
public class ChildController {
    private final ChildService service;

    // Get all children
    @GetMapping
    public ResponseEntity<ChildPageResponse> getAllChildren(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery
    ) {
        ChildPageResponse response = service.getAllChildren(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get all children without pagination
    @GetMapping("/get-all")
    public ResponseEntity<List<ChildPayloadDTO>> getAllChildrenNoPagination() {
        List<ChildPayloadDTO> children = service.getAllChildrenNoPagination();
        return new ResponseEntity<>(children, HttpStatus.OK);
    }

    // Get single child
    @GetMapping("/{childId}")
    public ResponseEntity<ChildPayloadDTO> getAChild(@PathVariable Long childId) {
        ChildPayloadDTO childDTO = service.getAChild(childId);
        return new ResponseEntity<>(childDTO, HttpStatus.OK);
    }

    // add new child
    @PostMapping
    public ResponseEntity<?> addChild(@Valid @RequestBody ChildPayloadDTO childDTO) {
        service.addChild(childDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update a child
    @PutMapping("/{childId}")
    public ResponseEntity<?> updateChild(@Valid @RequestBody ChildPayloadDTO childDTO, @PathVariable Long childId) {
        service.updateChild(childDTO, childId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a child
    @DeleteMapping("/{childId}")
    public ResponseEntity<?> deleteChild(@PathVariable Long childId) {
        service.deleteChild(childId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}