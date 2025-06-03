package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.billing.BillingPageResponse;
import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;
import com.ravi.waterlilly.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// handle all billing-related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/billing")
public class BillingController {
    private final BillingService billingService;

    // get all billing details
    @GetMapping
    public ResponseEntity<BillingPageResponse> getAllBillingDetails(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery
    ) {
        BillingPageResponse billingPageResponse = billingService.getAllBillingDetails(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(billingPageResponse, HttpStatus.OK);
    }

    // get single billing details
    @GetMapping("/{billingId}")
    public ResponseEntity<BillingPayloadDTO> getSingleBillingDetails(@PathVariable Long billingId) {
        BillingPayloadDTO billingPayloadDTO = billingService.getSingleBillingDetails(billingId);
        return new ResponseEntity<>(billingPayloadDTO, HttpStatus.OK);
    }

    // create a new billing detail
    @PostMapping
    public ResponseEntity<?> createBillingDetails(@RequestBody BillingPayloadDTO billingPayloadDTO) {
        billingService.createNewBillingDetails(billingPayloadDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update billing details
    @PutMapping("/{billingId}")
    public ResponseEntity<?> updateBillingDetails(@PathVariable Long billingId, @RequestBody BillingPayloadDTO billingPayloadDTO) {
        billingService.updateBillingDetails(billingId, billingPayloadDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    // delete billing details
//    @DeleteMapping("/{billingId}")
//    public ResponseEntity<?> deleteBillingDetails(@PathVariable Long billingId) {
//        billingService.deleteBillingDetails(billingId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

}
