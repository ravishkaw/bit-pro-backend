package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.PaymentStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;
import com.ravi.waterlilly.repository.PaymentStatusRepository;
import com.ravi.waterlilly.service.PaymentStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

// implementation of payment status service
@Service
@RequiredArgsConstructor
public class PaymentStatusServiceImpl implements PaymentStatusService {
    private final PaymentStatusRepository paymentStatusRepository;
    private final ModelMapper modelMapper;

    // get all statuses
    @Override
    public List<ReferenceDataDTO> getPaymentStatuses() {
        return paymentStatusRepository.findAll().stream()
                .map(status -> modelMapper.map(status, ReferenceDataDTO.class))
                .toList();
    }

    // get payment status by id
    @Override
    public PaymentStatus getPaymentStatusById(Integer id) {
        return paymentStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Status", "id", id.toString()));
    }

    // Helper method to setting payment status
    @Override
    public PaymentStatus getBillingStatus(BillingPayloadDTO billingPayloadDTO) {
        PaymentStatus paidStatus = getStatusByName("Paid");
        PaymentStatus partiallyPaidStatus = getStatusByName("Partially Paid");
        PaymentStatus pendingStatus = getStatusByName("Pending");

        BigDecimal totalPrice = billingPayloadDTO.getTotalPrice();
        BigDecimal paidAmount = billingPayloadDTO.getPaidAmount();

        if (paidAmount.equals(BigDecimal.ZERO)) {
            return pendingStatus;
        } else if (paidAmount.compareTo(totalPrice) >= 0) {
            return paidStatus;
        } else {
            return partiallyPaidStatus;
        }
    }

    // get payment status by name
    private PaymentStatus getStatusByName(String statusName) {
        PaymentStatus status = paymentStatusRepository.findByStatusName(statusName);
        if (status == null) {
            throw new ResourceNotFoundException("Payment Status", "name", statusName);
        }
        return status;
    }
}
