package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.LateCheckinPolicy;
import com.ravi.waterlilly.payload.reservationPolicy.LateCheckinPolicyDTO;
import com.ravi.waterlilly.repository.LateCheckinPolicyRepository;
import com.ravi.waterlilly.service.LateCheckinPolicyService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of Policy Service.
@Service
@RequiredArgsConstructor
public class LateCheckinPolicyServiceImpl implements LateCheckinPolicyService {

    private final PrivilegeUtils privilegeUtils;
    private final LateCheckinPolicyRepository lateCheckinPolicyRepository;
    private final ModelMapper modelMapper;

    // get all policy
    @Override
    public List<LateCheckinPolicyDTO> getAllPolicies() {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.SELECT);

        return lateCheckinPolicyRepository.findAll().stream()
                .map(policy -> modelMapper.map(policy, LateCheckinPolicyDTO.class))
                .toList();
    }

    // get single policy
    @Override
    public LateCheckinPolicyDTO getSinglePolicy(Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.SELECT);

        LateCheckinPolicy policy = getPolicyById(id);

        return modelMapper.map(policy, LateCheckinPolicyDTO.class);
    }

    // add new policy
    @Override
    public void addPolicy(LateCheckinPolicyDTO policyDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.INSERT);

        LateCheckinPolicy policy = modelMapper.map(policyDTO, LateCheckinPolicy.class);
        lateCheckinPolicyRepository.save(policy);
    }

    // update a policy
    @Override
    public void updatePolicy(LateCheckinPolicyDTO policyDTO, Integer policyId) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.UPDATE);

        LateCheckinPolicy policy = getPolicyById(policyId);
        modelMapper.map(policyDTO, policy);
        lateCheckinPolicyRepository.save(policy);
    }

    // delete a policy
    @Override
    public void deletePolicy(Integer policyId) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.DELETE);

        LateCheckinPolicy policy = getPolicyById(policyId);
        lateCheckinPolicyRepository.delete(policy);
    }

    // get policy by id
    @Override
    public LateCheckinPolicy getPolicyById(Integer id) {
        return lateCheckinPolicyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Late Checkin Policy", "id", id.toString()));
    }

    // get applicable policy for late checkin hours
    @Override
    public LateCheckinPolicy getApplicablePolicy(Integer hoursLate) {
        Pageable limitOne = PageRequest.of(0, 1);
        List<LateCheckinPolicy> policies = lateCheckinPolicyRepository.findApplicablePolicies(hoursLate, limitOne);
        return policies.isEmpty() ? null : policies.get(0);
    }
}
