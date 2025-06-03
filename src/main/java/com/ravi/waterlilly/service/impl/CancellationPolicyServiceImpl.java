package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.CancellationPolicy;
import com.ravi.waterlilly.payload.reservationPolicy.CancellationPolicyDTO;
import com.ravi.waterlilly.repository.CancellationPolicyRepository;
import com.ravi.waterlilly.service.CancellationPolicyService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of Cancellation Policy Service.
@Service
@RequiredArgsConstructor
public class CancellationPolicyServiceImpl implements CancellationPolicyService {

    private final PrivilegeUtils privilegeUtils;
    private final CancellationPolicyRepository cancellationPolicyRepository;
    private final ModelMapper modelMapper;

    // get all policies
    @Override
    public List<CancellationPolicyDTO> getAllPolicies() {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.SELECT);

        return cancellationPolicyRepository.findAll().stream()
                .map(policy -> modelMapper.map(policy, CancellationPolicyDTO.class))
                .toList();
    }

    // get single policy
    @Override
    public CancellationPolicyDTO getSinglePolicy(Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.SELECT);

        CancellationPolicy policy = getPolicyById(id);

        return modelMapper.map(policy, CancellationPolicyDTO.class);
    }

    // add new policy
    @Override
    public void addPolicy(CancellationPolicyDTO policyDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.INSERT);

        CancellationPolicy policy = modelMapper.map(policyDTO, CancellationPolicy.class);
        cancellationPolicyRepository.save(policy);
    }

    // update a policy
    @Override
    public void updatePolicy(CancellationPolicyDTO policyDTO, Integer policyId) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.UPDATE);

        CancellationPolicy policy = getPolicyById(policyId);
        modelMapper.map(policyDTO, policy);
        cancellationPolicyRepository.save(policy);
    }

    // delete a policy
    @Override
    public void deletePolicy(Integer policyId) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.DELETE);

        CancellationPolicy policy = getPolicyById(policyId);
        cancellationPolicyRepository.delete(policy);
    }

    // get policy by id
    @Override
    public CancellationPolicy getPolicyById(Integer id) {
        return cancellationPolicyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancellation Policy", "id", id.toString()));
    }

    // get applicable early checkout policy
    @Override
    public CancellationPolicy getApplicablePolicy(Integer hoursBeforeCheckin) {
        Pageable limitOne = PageRequest.of(0, 1);
        List<CancellationPolicy> policies = cancellationPolicyRepository.findApplicablePolicies(hoursBeforeCheckin, limitOne);
        return policies.isEmpty() ? null : policies.get(0);
    }
}
