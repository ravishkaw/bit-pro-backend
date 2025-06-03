package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.LateCheckinPolicy;
import com.ravi.waterlilly.payload.reservationPolicy.LateCheckinPolicyDTO;

import java.util.List;

// service layer of policy
public interface LateCheckinPolicyService {
    // get all policy
    List<LateCheckinPolicyDTO> getAllPolicies();

    // get single policy
    LateCheckinPolicyDTO getSinglePolicy(Integer id);

    // add new policy
    void addPolicy(LateCheckinPolicyDTO policyDTO);

    // update a policy
    void updatePolicy(LateCheckinPolicyDTO policyDTO, Integer policyId);

    // delete a policy
    void deletePolicy(Integer policyId);

    // get policy by id
    LateCheckinPolicy getPolicyById(Integer id);

    // get applicable policy for late checkin hours
    LateCheckinPolicy getApplicablePolicy(Integer hoursLate);
}
