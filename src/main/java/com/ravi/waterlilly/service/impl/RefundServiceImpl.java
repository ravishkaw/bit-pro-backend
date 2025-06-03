package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Refund;
import com.ravi.waterlilly.payload.billing.RefundPayloadDTO;
import com.ravi.waterlilly.repository.RefundRepository;
import com.ravi.waterlilly.service.BillingService;
import com.ravi.waterlilly.service.RefundService;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// implementation of refund service
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
    private final PrivilegeUtils privilegeUtils;
    private final RefundRepository refundRepository;
    private final ModelMapper modelMapper;
    private final BillingService billingService;

    // get all refunds
    @Override
    public List<RefundPayloadDTO> getAllRefunds() {

        // check privileges
        privilegeUtils.privilegeCheck("Billing", AppConstants.SELECT);

        // map and return refunds
        return refundRepository.findAll().stream()
                .map(refund -> modelMapper.map(refund, RefundPayloadDTO.class))
                .toList();
    }

    // get a single refund
    @Override
    public RefundPayloadDTO getSingleRefund(Integer id) {

        // check privileges
        privilegeUtils.privilegeCheck("Billing", AppConstants.SELECT);

        // map and return refund
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund", "refundId", id.toString()));

        return modelMapper.map(refund, RefundPayloadDTO.class);
    }

    // add a new refund
    @Override
    public void addRefund(RefundPayloadDTO refundPayloadDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Billing", AppConstants.INSERT);

        Refund refund = new Refund();
        updateRefundFields(refund, refundPayloadDTO);
        refundRepository.save(refund);
    }

    // Helper method to update refund fields
    private void updateRefundFields(Refund refund, RefundPayloadDTO refundPayloadDTO) {
        refund.setBilling(billingService.getBillingRecordById(refundPayloadDTO.getBilling().getId()));
        refund.setAmount(refundPayloadDTO.getAmount());
        refund.setReason(refundPayloadDTO.getReason());
        refund.setRefundDatetime(LocalDateTime.now());
    }
}
