package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.PaymentStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;

import java.util.List;

// service layer of payment status
public interface PaymentStatusService {

    // get all payment status
    List<ReferenceDataDTO> getPaymentStatuses();

    // get payment status by id
    PaymentStatus getPaymentStatusById(Integer id);

    // Helper method to setting payment status
    PaymentStatus getBillingStatus(BillingPayloadDTO billingPayloadDTO);
}
