package com.ravi.waterlilly.payload.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Billing DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingPayloadDTO {
    private Long id;
    private BigDecimal basePrice;
    private Integer discountId;
    private BigDecimal discountAmount;
    private Integer taxId;
    private BigDecimal totalTax;
    private BigDecimal totalPrice;
    private BigDecimal netAmount;
    private Integer paymentMethodId;
    private BigDecimal paidAmount;
    private LocalDateTime billingDate;
    private String note;
}
