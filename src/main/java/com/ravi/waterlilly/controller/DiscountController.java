package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.billingDiscount.DiscountPayloadDTO;
import com.ravi.waterlilly.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handle discount related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/discounts")
public class DiscountController {
    private final DiscountService discountService;

    // get all discounts
    @GetMapping
    public ResponseEntity<List<DiscountPayloadDTO>> getAllDiscounts() {
        List<DiscountPayloadDTO> discountPayloadDTOS = discountService.getAllDiscounts();
        return new ResponseEntity<>(discountPayloadDTOS, HttpStatus.OK);
    }

    // get a single discount
    @GetMapping("/{discountId}")
    public ResponseEntity<DiscountPayloadDTO> getDiscount(@PathVariable Integer discountId) {
        DiscountPayloadDTO discountPayloadDTO = discountService.getSingleDiscount(discountId);
        return new ResponseEntity<>(discountPayloadDTO, HttpStatus.OK);
    }

    // add new discount
    @PostMapping
    public ResponseEntity<?> createDiscount(@Valid DiscountPayloadDTO discountPayloadDTO) {
        discountService.addDiscount(discountPayloadDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update discount
    @PutMapping("/{discountId}")
    public ResponseEntity<?> updateDiscount(@PathVariable Integer discountId, @Valid @RequestBody DiscountPayloadDTO discountPayloadDTO) {
        discountService.updateDiscount(discountId, discountPayloadDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete discount
    @DeleteMapping("/{discountId}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Integer discountId) {
        discountService.deleteDiscount(discountId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // restore discount
    @PutMapping("/{discountId}/restore")
    public ResponseEntity<?> restoreDiscount(@PathVariable Integer discountId) {
        discountService.restoreDiscount(discountId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
