package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.RoomReservationAmenityCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle room reservation amenity categories api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/room-reservation-amenity-categories")
public class RoomReservationAmenityCategoryController {
    private final RoomReservationAmenityCategoryService roomReservationAmenityCategoryService;

    // fetch all categories
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAll() {
        List<ReferenceDataDTO> categoriesDTOS = roomReservationAmenityCategoryService.getAllCategories();
        return new ResponseEntity<>(categoriesDTOS, HttpStatus.OK);
    }
}
