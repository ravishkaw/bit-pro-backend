package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.roomReservation.*;
import com.ravi.waterlilly.payload.roomReservationBilling.CheckOutBillingDTO;
import com.ravi.waterlilly.payload.roomReservationBilling.RoomReservationBillingDTO;
import com.ravi.waterlilly.payload.roomReservationBilling.RoomReservationBillingPayloadDTO;
import com.ravi.waterlilly.service.RoomReservationBillingService;
import com.ravi.waterlilly.service.RoomReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// handle all room-reservation-related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-reservation")
public class RoomReservationController {
    private final RoomReservationService roomReservationService;
    private final RoomReservationBillingService roomReservationBillingService;

    // get all reservations
    @GetMapping
    public ResponseEntity<RoomReservationPageResponse> getAllRoomReservations(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery,
            @RequestParam(name = "status", defaultValue = "CHECKED-IN") String status
    ) {
        RoomReservationPageResponse roomReservationPageResponse = roomReservationService.getAllReservations(pageNumber, pageSize, sortBy, sortOrder, status, searchQuery);
        return new ResponseEntity<>(roomReservationPageResponse, HttpStatus.OK);
    }

    // get a single reservation
    @GetMapping("/{reservationId}")
    public ResponseEntity<RoomReservationPayloadDTO> getSingleReservation(@PathVariable Long reservationId) {
        RoomReservationPayloadDTO roomReservationPayloadDTO = roomReservationService.getSingleReservation(reservationId);
        return new ResponseEntity<>(roomReservationPayloadDTO, HttpStatus.OK);
    }

    // add new reservation
    @PostMapping
    public ResponseEntity<String> addRoomReservation(@RequestBody RoomReservationPayloadFormDTO formDTO) {
        roomReservationService.addRoomReservation(formDTO);
        return new ResponseEntity<>("Room reservation added successfully", HttpStatus.CREATED);
    }

    // update existing reservation
    @PutMapping("/{reservationId}")
    public ResponseEntity<String> updateRoomReservation(
            @PathVariable Long reservationId,
            @RequestBody RoomReservationPayloadFormDTO formDTO) {
        roomReservationService.updateRoomReservation(reservationId, formDTO);
        return new ResponseEntity<>("Room reservation updated successfully", HttpStatus.OK);
    }

    // calculate reservation pricing
    @PostMapping("/calculate-pricing")
    public ResponseEntity<RoomReservationBillingDTO> calculateReservationPricing(
            @RequestBody RoomReservationBillingPayloadDTO billingDTO) {
        RoomReservationBillingDTO calculatedPricing = roomReservationBillingService.calculatePrice(billingDTO);
        return new ResponseEntity<>(calculatedPricing, HttpStatus.OK);
    }

    // calculate reservation pricing for checkout
    @PostMapping("/{reservationId}/calculate-pricing")
    public ResponseEntity<CheckOutBillingDTO> calculateCheckoutPricing(@PathVariable Long reservationId) {
        CheckOutBillingDTO calculatedPricing = roomReservationBillingService.calculatePriceForCheckOut(reservationId);
        return new ResponseEntity<>(calculatedPricing, HttpStatus.OK);
    }

    // update reservation status ( actions )
    @PutMapping("/{reservationId}/{action}")
    public ResponseEntity<?> updateStatusToAction(@PathVariable Long reservationId, @PathVariable String action) {
        roomReservationService.updateStatusToAction(reservationId, action);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // reservation check out
    @PutMapping("/{reservationId}/check-out")
    public ResponseEntity<String> reservationCheckOut(@PathVariable Long reservationId, @RequestBody CheckOutDTO checkOutDTO) {
        String receiptUrl = roomReservationService.reservationCheckOut(reservationId, checkOutDTO);
        return new ResponseEntity<>(receiptUrl, HttpStatus.OK);
    }
}
