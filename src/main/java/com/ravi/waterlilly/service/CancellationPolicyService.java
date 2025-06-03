package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.CancellationPolicy;
import com.ravi.waterlilly.payload.reservationPolicy.CancellationPolicyDTO;

import java.util.List;

// service layer of policy
public interface CancellationPolicyService {
    // get all policies
    List<CancellationPolicyDTO> getAllPolicies();

    // get single policy
    CancellationPolicyDTO getSinglePolicy(Integer id);

    // add new policy
    void addPolicy(CancellationPolicyDTO policyDTO);

    // update a policy
    void updatePolicy(CancellationPolicyDTO policyDTO, Integer policyId);

    // delete a policy
    void deletePolicy(Integer policyId);

    // get policy by id
    CancellationPolicy getPolicyById(Integer id);

    // get applicable early checkout policy
    CancellationPolicy getApplicablePolicy(Integer hoursBeforeCheckin);
}
