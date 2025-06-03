package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.PaymentMethod;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.PaymentMethodRepository;
import com.ravi.waterlilly.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of payment method service
@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final ModelMapper modelMapper;

    // get all payment methods
    @Override
    public List<ReferenceDataDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAll().stream()
                .map(method -> modelMapper.map(method, ReferenceDataDTO.class))
                .toList();
    }

    // get method by id
    @Override
    public PaymentMethod getPaymentMethodById(Integer id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Method", "id", id.toString()));
    }
}
