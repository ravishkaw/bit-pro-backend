package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Billing;
import com.ravi.waterlilly.payload.billing.BillingPageResponse;
import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;

// Service layer of Billing
public interface BillingService {

    // get all billing details
    BillingPageResponse getAllBillingDetails(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // get single billing details
    BillingPayloadDTO getSingleBillingDetails(Long id);

    // create new billing details
    Billing createNewBillingDetails(BillingPayloadDTO billingPayloadDTO);

    // update billing details
    Billing updateBillingDetails(Long id, BillingPayloadDTO billingPayloadDTO);

    // get billing record by id
    Billing getBillingRecordById(Long id);
}
