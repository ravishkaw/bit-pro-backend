package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.PaymentStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handle all payment status related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/payment-status")
public class PaymentStatusController {
    private final PaymentStatusService paymentStatusService;

    // get all status
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getPaymentStatuses() {
        List<ReferenceDataDTO> paymentStatuses = paymentStatusService.getPaymentStatuses();
        return new ResponseEntity<>(paymentStatuses, HttpStatus.OK);
    }
}
