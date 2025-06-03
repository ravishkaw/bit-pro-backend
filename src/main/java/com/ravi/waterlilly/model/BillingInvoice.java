package com.ravi.waterlilly.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// billing invoice class
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingInvoice {
    private Long id;
    private String invoiceNumber;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private Billing billing;
}