package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.room.RoomCardForReservationDTO;
import com.ravi.waterlilly.payload.room.RoomPayloadDTO;
import com.ravi.waterlilly.payload.room.RoomCardDTO;
import com.ravi.waterlilly.payload.room.RoomReferenceDTO;
import com.ravi.waterlilly.payload.roomType.RoomTypePayloadDTO;
import com.ravi.waterlilly.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// handle all room related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    // Get all rooms to the format room card dto
    @GetMapping
    public ResponseEntity<List<RoomCardDTO>> getAllRooms() {
        List<RoomCardDTO> rooms = roomService.getAllRooms();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    // Get all rooms with reference data only
    @GetMapping("/get-all")
    public ResponseEntity<List<RoomReferenceDTO>> getAll() {
        List<RoomReferenceDTO> rooms = roomService.getAll();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    // filter rooms
    @GetMapping("/filter")
    public ResponseEntity<List<RoomCardDTO>> filterRooms(
            @RequestParam(name = "roomTypeId", required = false) Integer roomTypeId,
            @RequestParam(name = "statusId", required = false) Integer statusId,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "adults", required = false) Integer adults,
            @RequestParam(name = "children", required = false) Integer children,
            @RequestParam(name = "infants", required = false) Integer infants,
            @RequestParam(name = "searchQuery", required = false) String searchQuery
    ) {
        List<RoomCardDTO> filteredRooms = roomService.filterRooms(
                roomTypeId, statusId, minPrice, maxPrice, adults, children, infants, searchQuery);
        return new ResponseEntity<>(filteredRooms, HttpStatus.OK);
    }

    // get a single room details
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomPayloadDTO> getSingleRoom(@PathVariable Long roomId) {
        RoomPayloadDTO room = roomService.getSingleRoom(roomId);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    // get available rooms for a stay between two dates with adults, children and infants
    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomCardForReservationDTO>> getAvailableRooms(
            @RequestParam(name = "checkInDate") LocalDate checkInDate,
            @RequestParam(name = "checkOutDate") LocalDate checkOutDate,
            @RequestParam(name = "adults", defaultValue = "1") Integer adults,
            @RequestParam(name = "children", defaultValue = "0") Integer children,
            @RequestParam(name = "infants", defaultValue = "0") Integer infants
    ) {
        List<RoomCardForReservationDTO> roomCardDTOS = roomService.getAvailableRooms(checkInDate, checkOutDate, adults, children, infants);
        return new ResponseEntity<>(roomCardDTOS, HttpStatus.OK);
    }

    // add new room
    @PostMapping
    public ResponseEntity<RoomPayloadDTO> addNewRoom(@RequestBody RoomPayloadDTO room) throws IOException {
        roomService.addNewRoom(room);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Update room
    @PutMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(@RequestBody RoomPayloadDTO roomPayloadDTO, @PathVariable Long roomId) throws IOException {
        roomService.updateRoom(roomPayloadDTO, roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Deletes room
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Restores a deleted room.
    @PutMapping("/{roomId}/restore")
    public ResponseEntity<?> restoreRoom(@PathVariable Long roomId) {
        roomService.restoreRoom(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
