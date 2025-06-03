package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.roomPricingRule.RoomPricingRulePageResponse;
import com.ravi.waterlilly.payload.roomPricingRule.RoomPricingRulePayloadDTO;
import com.ravi.waterlilly.service.RoomPricingRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Handles all room pricing rule related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/pricing-rules")
public class RoomPricingRuleController {
    private final RoomPricingRuleService roomPricingRuleService;

    // get all rules
    @GetMapping
    public ResponseEntity<RoomPricingRulePageResponse> getAllRules(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery) {
        RoomPricingRulePageResponse pricingRules = roomPricingRuleService.getAllRules(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(pricingRules, HttpStatus.OK);
    }

    //Get single pricing rules
    @GetMapping("/{pricingRuleId}")
    public ResponseEntity<RoomPricingRulePayloadDTO> getSingleRule(@PathVariable Integer pricingRuleId) {
        RoomPricingRulePayloadDTO pricingRule = roomPricingRuleService.getSingleRule(pricingRuleId);
        return new ResponseEntity<>(pricingRule, HttpStatus.OK);
    }

    //Add new pricing rules
    @PostMapping
    public ResponseEntity<?> addNewRule(@Valid @RequestBody RoomPricingRulePayloadDTO roomPricingRuleDTO) {
        roomPricingRuleService.addNewRule(roomPricingRuleDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Update pricing rules
    @PutMapping("/{pricingRuleId}")
    public ResponseEntity<?> updateRule(@Valid @RequestBody RoomPricingRulePayloadDTO roomPricingRuleDTO, @PathVariable Integer pricingRuleId) {
        roomPricingRuleService.updateRule(roomPricingRuleDTO, pricingRuleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Deletes pricing rules
    @DeleteMapping("/{pricingRuleId}")
    public ResponseEntity<?> deleteRule(@PathVariable Integer pricingRuleId) {
        roomPricingRuleService.deleteRule(pricingRuleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Restores a deleted pricingRule.
    @PutMapping("/{pricingRuleId}/restore")
    public ResponseEntity<?> restorePricingRule(@PathVariable Integer pricingRuleId) {
        roomPricingRuleService.restorePricingRule(pricingRuleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
