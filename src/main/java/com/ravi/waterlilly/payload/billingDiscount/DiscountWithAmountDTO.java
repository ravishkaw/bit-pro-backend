package com.ravi.waterlilly.payload.billingDiscount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// discount with amount dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountWithAmountDTO {
    private DiscountReferenceDTO discount;
    private BigDecimal amount;
}
