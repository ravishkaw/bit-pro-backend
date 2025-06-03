package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.PaymentMethodService;
import com.ravi.waterlilly.service.RoomStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handles all room payment method-related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    // Fetches a list of payment method.
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllPaymentMethod() {
        List<ReferenceDataDTO> paymentMethods = paymentMethodService.getAllPaymentMethods();
        return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
    }
}
