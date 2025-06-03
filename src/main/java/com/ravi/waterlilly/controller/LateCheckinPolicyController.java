package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.reservationPolicy.LateCheckinPolicyDTO;
import com.ravi.waterlilly.service.LateCheckinPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// this controller handles all late check-in policy related api
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/late-checkin-policies")
public class LateCheckinPolicyController {
    private final LateCheckinPolicyService lateCheckinPolicyService;

    // get all late check-in policies
    @GetMapping
    public ResponseEntity<List<LateCheckinPolicyDTO>> getAllLateCheckinPolicies() {
        List<LateCheckinPolicyDTO> policies = lateCheckinPolicyService.getAllPolicies();
        return new ResponseEntity<>(policies, HttpStatus.OK);
    }

    // get single late check-in policy
    @GetMapping("/{policyId}")
    public ResponseEntity<LateCheckinPolicyDTO> getLateCheckinPolicyById(@PathVariable Integer policyId) {
        LateCheckinPolicyDTO policy = lateCheckinPolicyService.getSinglePolicy(policyId);
        return new ResponseEntity<>(policy, HttpStatus.OK);
    }

    // add a late check-in policy
    @PostMapping
    public ResponseEntity<?> addLateCheckinPolicy(@RequestBody LateCheckinPolicyDTO policyDTO) {
        lateCheckinPolicyService.addPolicy(policyDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // update a late check-in policy
    @PutMapping("/{policyId}")
    public ResponseEntity<?> updateLateCheckinPolicy(@PathVariable Integer policyId, @RequestBody LateCheckinPolicyDTO policyDTO) {
        lateCheckinPolicyService.updatePolicy(policyDTO, policyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a late check-in policy
    @DeleteMapping("/{policyId}")
    public ResponseEntity<?> deleteLateCheckinPolicy(@PathVariable Integer policyId) {
        lateCheckinPolicyService.deletePolicy(policyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
