package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.billing.BillingPageResponse;
import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;
import com.ravi.waterlilly.payload.billing.BillingTableData;
import com.ravi.waterlilly.repository.*;
import com.ravi.waterlilly.service.*;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// implementation of billing service
@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final PrivilegeUtils privilegeUtils;
    private final BillingRepository billingRepository;
    private final ModelMapper modelMapper;
    private final PaymentMethodService paymentMethodService;
    private final TaxService taxService;
    private final PaymentStatusService paymentStatusService;
    private final DiscountService discountService;

    // get all billing details
    @Override
    public BillingPageResponse getAllBillingDetails(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // check privileges
        privilegeUtils.privilegeCheck("Billing", AppConstants.SELECT);

        // determine sort order
        Sort sort = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // create a pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Billing> billingPage = billingRepository.findAll(pageable);

        // extract content from the page
        List<Billing> billingList = billingPage.getContent();

        // map list of billing
        List<BillingTableData> tableData = billingList.stream()
                .map(billing -> modelMapper.map(billing, BillingTableData.class))
                .toList();

        return new BillingPageResponse(
                tableData,
                billingPage.getNumber(),
                billingPage.getSize(),
                billingPage.getTotalElements(),
                billingPage.getTotalPages(),
                billingPage.isLast()
        );
    }

    // get single billing details
    @Override
    public BillingPayloadDTO getSingleBillingDetails(Long id) {

        // check privileges
        privilegeUtils.privilegeCheck("Billing", AppConstants.SELECT);

        // find and map billing details
        Billing billingRecord = getBillingRecordById(id);

        return modelMapper.map(billingRecord, BillingPayloadDTO.class);
    }

    // create new billing details
    @Transactional
    @Override
    public Billing createNewBillingDetails(BillingPayloadDTO billingPayloadDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Billing", AppConstants.INSERT);

        Billing newBilling = new Billing();
        updateBillingDetails(newBilling, billingPayloadDTO);
        billingRepository.save(newBilling);
        return newBilling;
    }

    // update billing details
    @Transactional
    @Override
    public Billing updateBillingDetails(Long id, BillingPayloadDTO billingPayloadDTO) {

        // check privileges
        privilegeUtils.privilegeCheck("Billing", AppConstants.UPDATE);

        Billing existingBilling = getBillingRecordById(id);

        updateBillingDetails(existingBilling, billingPayloadDTO);
        billingRepository.save(existingBilling);
        return existingBilling;
    }

    // get billing record by id
    @Override
    public Billing getBillingRecordById(Long id) {
        return billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billing", "Billing Id", id));
    }

    // Helper method to update billing details
    private void updateBillingDetails(Billing billing, BillingPayloadDTO billingPayloadDTO) {
        billing.setBasePrice(billingPayloadDTO.getBasePrice());

        // Only set discount if discountId is not null
        if (billingPayloadDTO.getDiscountId() != null) {
            billing.setDiscount(discountService.getDiscountById(billingPayloadDTO.getDiscountId()));
        } else {
            billing.setDiscount(null);
        }

        billing.setDiscountAmount(billingPayloadDTO.getDiscountAmount());
        billing.setTax(taxService.getTaxById(billingPayloadDTO.getTaxId()));
        billing.setTotalTax(billingPayloadDTO.getTotalTax());
        billing.setTotalPrice(billingPayloadDTO.getTotalPrice());
        billing.setNetAmount(billingPayloadDTO.getNetAmount());
        billing.setPaymentMethod(paymentMethodService.getPaymentMethodById(billingPayloadDTO.getPaymentMethodId()));
        billing.setPaidAmount(billingPayloadDTO.getPaidAmount());
        billing.setPaymentStatus(paymentStatusService.getBillingStatus(billingPayloadDTO));
        billing.setNote(billingPayloadDTO.getNote());

        LocalDateTime now = LocalDateTime.now();
        if (billing.getId() == null) {
            billing.setBillingDate(now);
        }
        billing.setLastModifiedDatetime(now);
    }
}