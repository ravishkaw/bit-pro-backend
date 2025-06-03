package com.ravi.waterlilly.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// billing receipt class
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingReceipt {
    private Long id;
    private String receiptNumber;
    private LocalDateTime issueDate;
    private Billing billing;
    private User user;
}
