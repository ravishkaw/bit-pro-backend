package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.billingTaxes.TaxPayloadDTO;
import com.ravi.waterlilly.service.TaxService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handle tax related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/tax")
public class TaxController {
    private final TaxService taxService;

    // get all taxes
    @GetMapping
    public ResponseEntity<List<TaxPayloadDTO>> getAllTaxes() {
        List<TaxPayloadDTO> getAllTaxes = taxService.getAllTaxes();
        return new ResponseEntity<>(getAllTaxes, HttpStatus.OK);
    }

    // get single tax
    @GetMapping("/{taxId}")
    public ResponseEntity<TaxPayloadDTO> getTax(@PathVariable Integer taxId) {
        TaxPayloadDTO tax = taxService.getTax(taxId);
        return new ResponseEntity<>(tax, HttpStatus.OK);
    }

    // add new tax
    @PostMapping
    public ResponseEntity<?> addTax(@Valid @RequestBody TaxPayloadDTO taxPayloadDTO) {
        taxService.addTax(taxPayloadDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update tax
    @PutMapping("/{taxId}")
    public ResponseEntity<?> updateTax(@PathVariable Integer taxId, @Valid @RequestBody TaxPayloadDTO taxPayloadDTO) {
        taxService.updateTax(taxId, taxPayloadDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete tax
    @DeleteMapping("/{taxId}")
    public ResponseEntity<?> deleteTax(@PathVariable Integer taxId) {
        taxService.deleteTax(taxId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // restore tax
    @PutMapping("/{taxId}/restore")
    public ResponseEntity<?> restoreTax(@PathVariable Integer taxId) {
        taxService.restoreTax(taxId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
