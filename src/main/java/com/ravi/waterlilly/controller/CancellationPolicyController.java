package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.reservationPolicy.CancellationPolicyDTO;
import com.ravi.waterlilly.service.CancellationPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// this controller handles all cancellation policy related api
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/cancellation-policies")
public class CancellationPolicyController {
    private final CancellationPolicyService cancellationPolicyService;

    // get all cancellation policies
    @GetMapping
    public ResponseEntity<List<CancellationPolicyDTO>> getAllCancellationPolicies() {
        List<CancellationPolicyDTO> policies = cancellationPolicyService.getAllPolicies();
        return new ResponseEntity<>(policies, HttpStatus.OK);
    }

    // get single cancellation policy
    @GetMapping("/{policyId}")
    public ResponseEntity<CancellationPolicyDTO> getCancellationPolicyById(@PathVariable Integer policyId) {
        CancellationPolicyDTO policy = cancellationPolicyService.getSinglePolicy(policyId);
        return new ResponseEntity<>(policy, HttpStatus.OK);
    }

    // add a cancellation policy
    @PostMapping
    public ResponseEntity<?> addCancellationPolicy(@RequestBody CancellationPolicyDTO policyDTO) {
        cancellationPolicyService.addPolicy(policyDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // update a cancellation policy
    @PutMapping("/{policyId}")
    public ResponseEntity<?> updateCancellationPolicy(@PathVariable Integer policyId, @RequestBody CancellationPolicyDTO policyDTO) {
        cancellationPolicyService.updatePolicy(policyDTO, policyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a cancellation policy
    @DeleteMapping("/{policyId}")
    public ResponseEntity<?> deleteCancellationPolicy(@PathVariable Integer policyId) {
        cancellationPolicyService.deletePolicy(policyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
