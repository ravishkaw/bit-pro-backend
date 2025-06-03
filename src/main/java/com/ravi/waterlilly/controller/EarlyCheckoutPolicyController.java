package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.reservationPolicy.EarlyCheckoutPolicyDTO;
import com.ravi.waterlilly.service.EarlyCheckoutPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// this controller handles all early checkout policy related api
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/early-checkout-policies")
public class EarlyCheckoutPolicyController {
    private final EarlyCheckoutPolicyService earlyCheckoutPolicyService;

    // get all early checkout policies
    @GetMapping
    public ResponseEntity<List<EarlyCheckoutPolicyDTO>> getAllEarlyCheckoutPolicies() {
        List<EarlyCheckoutPolicyDTO> policies = earlyCheckoutPolicyService.getAllPolicies();
        return new ResponseEntity<>(policies, HttpStatus.OK);
    }

    // get single early checkout policy
    @GetMapping("/{policyId}")
    public ResponseEntity<EarlyCheckoutPolicyDTO> getEarlyCheckoutPolicyById(@PathVariable Integer policyId) {
        EarlyCheckoutPolicyDTO policy = earlyCheckoutPolicyService.getSinglePolicy(policyId);
        return new ResponseEntity<>(policy, HttpStatus.OK);
    }

    // add an early checkout policy
    @PostMapping
    public ResponseEntity<?> addEarlyCheckoutPolicy(@RequestBody EarlyCheckoutPolicyDTO policyDTO) {
        earlyCheckoutPolicyService.addPolicy(policyDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // update an early checkout policy
    @PutMapping("/{policyId}")
    public ResponseEntity<?> updateEarlyCheckoutPolicy(@PathVariable Integer policyId, @RequestBody EarlyCheckoutPolicyDTO policyDTO) {
        earlyCheckoutPolicyService.updatePolicy(policyDTO, policyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete an early checkout policy
    @DeleteMapping("/{policyId}")
    public ResponseEntity<?> deleteEarlyCheckoutPolicy(@PathVariable Integer policyId) {
        earlyCheckoutPolicyService.deletePolicy(policyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
