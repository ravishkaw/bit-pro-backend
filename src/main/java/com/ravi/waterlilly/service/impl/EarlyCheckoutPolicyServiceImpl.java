package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.EarlyCheckoutPolicy;
import com.ravi.waterlilly.payload.reservationPolicy.EarlyCheckoutPolicyDTO;
import com.ravi.waterlilly.repository.EarlyCheckoutPolicyRepository;
import com.ravi.waterlilly.service.EarlyCheckoutPolicyService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of Early Checkout Policy Service.
@Service
@RequiredArgsConstructor
public class EarlyCheckoutPolicyServiceImpl implements EarlyCheckoutPolicyService {

    private final PrivilegeUtils privilegeUtils;
    private final EarlyCheckoutPolicyRepository earlyCheckoutPolicyRepository;
    private final ModelMapper modelMapper;

    // get all policies
    @Override
    public List<EarlyCheckoutPolicyDTO> getAllPolicies() {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.SELECT);

        return earlyCheckoutPolicyRepository.findAll().stream()
                .map(policy -> modelMapper.map(policy, EarlyCheckoutPolicyDTO.class))
                .toList();
    }

    // get single policy
    @Override
    public EarlyCheckoutPolicyDTO getSinglePolicy(Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.SELECT);

        EarlyCheckoutPolicy policy = getPolicyById(id);

        return modelMapper.map(policy, EarlyCheckoutPolicyDTO.class);
    }

    // add new policy
    @Override
    public void addPolicy(EarlyCheckoutPolicyDTO policyDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.INSERT);

        EarlyCheckoutPolicy policy = modelMapper.map(policyDTO, EarlyCheckoutPolicy.class);
        earlyCheckoutPolicyRepository.save(policy);
    }

    // update a policy
    @Override
    public void updatePolicy(EarlyCheckoutPolicyDTO policyDTO, Integer policyId) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.UPDATE);

        EarlyCheckoutPolicy policy = getPolicyById(policyId);
        modelMapper.map(policyDTO, policy);
        earlyCheckoutPolicyRepository.save(policy);
    }

    // delete a policy
    @Override
    public void deletePolicy(Integer policyId) {

        // check privileges
        privilegeUtils.privilegeCheck("Reservation Policy", AppConstants.DELETE);

        EarlyCheckoutPolicy policy = getPolicyById(policyId);
        earlyCheckoutPolicyRepository.delete(policy);
    }

    // get policy by id
    @Override
    public EarlyCheckoutPolicy getPolicyById(Integer id) {
        return earlyCheckoutPolicyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Early Checkout Policy", "id", id.toString()));
    }

    // get applicable early checkout policy
    @Override
    public EarlyCheckoutPolicy getApplicablePolicy(Integer reservedNights, Integer actualNights, Integer daysEarly) {
        Pageable limitOne = PageRequest.of(0, 1);
        List<EarlyCheckoutPolicy> policies = earlyCheckoutPolicyRepository.findApplicablePolicy(
                reservedNights, actualNights, daysEarly, limitOne
        );
        return policies.isEmpty() ? null : policies.get(0);
    }
}
