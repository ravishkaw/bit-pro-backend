package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.roomType.RoomTypeBasicDTO;
import com.ravi.waterlilly.payload.roomType.RoomTypePayloadDTO;
import com.ravi.waterlilly.payload.roomType.RoomTypePageResponse;
import com.ravi.waterlilly.service.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handle all room type related API requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-types")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    //Get all room types with pagination
    @GetMapping
    public ResponseEntity<RoomTypePageResponse> getAllRoomTypes(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery) {
        RoomTypePageResponse roomTypes = roomTypeService.getAllRoomTypes(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(roomTypes, HttpStatus.OK);
    }

    //Get all room types without pagination
    @GetMapping("/get-all")
    public ResponseEntity<List<RoomTypeBasicDTO>> getRoomTypes() {
        List<RoomTypeBasicDTO> roomTypes = roomTypeService.getRoomTypes();
        return new ResponseEntity<>(roomTypes, HttpStatus.OK);
    }

    //Get single room type
    @GetMapping("/{roomTypeId}")
    public ResponseEntity<RoomTypePayloadDTO> getSingleRoomType(@PathVariable Integer roomTypeId) {
        RoomTypePayloadDTO roomTypes = roomTypeService.getSingleRoomType(roomTypeId);
        return new ResponseEntity<>(roomTypes, HttpStatus.OK);
    }

    //Add new room type
    @PostMapping
    public ResponseEntity<?> addNewRoomType(@Valid @RequestBody RoomTypePayloadDTO roomTypeDTO) {
        roomTypeService.addNewRoomType(roomTypeDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Update room type
    @PutMapping("/{roomTypeId}")
    public ResponseEntity<?> updateRoomType(@Valid @RequestBody RoomTypePayloadDTO roomTypeDTO, @PathVariable Integer roomTypeId) {
        roomTypeService.updateRoomType(roomTypeDTO, roomTypeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Deletes room type
    @DeleteMapping("/{roomTypeId}")
    public ResponseEntity<?> deleteRoomType(@PathVariable Integer roomTypeId) {
        roomTypeService.deleteRoomType(roomTypeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Restores a deleted roomType.
    @PutMapping("/{roomTypeId}/restore")
    public ResponseEntity<?> restoreRoomType(@PathVariable Integer roomTypeId) {
        roomTypeService.restoreRoomType(roomTypeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
