package com.ravi.waterlilly.service;

public interface BillingReceiptService {
    // Generates and stores a detailed checkout receipt
    String generateAndStoreCheckoutReceipt(Long reservationId);
}
