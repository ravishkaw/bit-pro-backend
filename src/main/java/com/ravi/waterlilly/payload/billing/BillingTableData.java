package com.ravi.waterlilly.payload.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Billing Table Data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingTableData {
    private Long id;
    private BigDecimal totalPrice;
    private String paymentMethodName;
    private BigDecimal paidAmount;
    private String paymentStatusName;
}
