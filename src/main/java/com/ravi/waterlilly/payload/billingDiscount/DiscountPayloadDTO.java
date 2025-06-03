package com.ravi.waterlilly.payload.billingDiscount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// discount dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountPayloadDTO {
    private Integer id;
    private String code;
    private BigDecimal percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String statusName;
}