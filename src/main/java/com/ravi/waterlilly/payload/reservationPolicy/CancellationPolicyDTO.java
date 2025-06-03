package com.ravi.waterlilly.payload.reservationPolicy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// cancellation policy class
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancellationPolicyDTO {
    private Integer id;
    private Integer minHoursBeforeCheckin;
    private BigDecimal refundPercentage;
    private BigDecimal cancellationFee;
}