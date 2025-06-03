package com.ravi.waterlilly.payload.reservationPolicy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// early checkout policy class
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarlyCheckoutPolicyDTO {
    private Integer id;
    private String policyName;
    private Integer minReservedNights;
    private Integer maxReservedNights;
    private Integer minActualNights;
    private Integer maxActualNights;
    private Integer minDaysBeforeCheckout;
    private BigDecimal earlyCheckoutFee;
    private BigDecimal partialRefundPercentage;
    private Integer statusId;
}