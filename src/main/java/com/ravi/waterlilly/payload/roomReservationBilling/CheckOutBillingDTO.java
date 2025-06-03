package com.ravi.waterlilly.payload.roomReservationBilling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO for check out billing pricing check
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutBillingDTO {
    private BigDecimal basePrice;
    private Integer discountId;
    private BigDecimal discountAmount;
    private Integer taxId;
    private BigDecimal totalTax;
    private BigDecimal totalPrice;
    private BigDecimal netAmount;
    private BigDecimal paidAmount;
    private Integer totalNights;
    private LocalDateTime checkOutDate;
    private String additionalNotes;
    private BigDecimal refundAmount;
}