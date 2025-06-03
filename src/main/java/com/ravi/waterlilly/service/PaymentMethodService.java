package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.PaymentMethod;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing payment method service.
public interface PaymentMethodService {

    //Fetches a list of payment methods.
    List<ReferenceDataDTO> getAllPaymentMethods();

    // get method by id
    PaymentMethod getPaymentMethodById(Integer id);
}
