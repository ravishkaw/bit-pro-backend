package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.EarlyCheckoutPolicy;
import com.ravi.waterlilly.payload.reservationPolicy.EarlyCheckoutPolicyDTO;

import java.util.List;

// service layer of policy
public interface EarlyCheckoutPolicyService {
    // get all policies
    List<EarlyCheckoutPolicyDTO> getAllPolicies();

    // get single policy
    EarlyCheckoutPolicyDTO getSinglePolicy(Integer id);

    // add new policy
    void addPolicy(EarlyCheckoutPolicyDTO policyDTO);

    // update a policy
    void updatePolicy(EarlyCheckoutPolicyDTO policyDTO, Integer policyId);

    // delete a policy
    void deletePolicy(Integer policyId);

    // get policy by id
    EarlyCheckoutPolicy getPolicyById(Integer id);

    // get applicable early checkout policy
    EarlyCheckoutPolicy getApplicablePolicy(Integer reservedNights, Integer actualNights, Integer daysEarly);
}
