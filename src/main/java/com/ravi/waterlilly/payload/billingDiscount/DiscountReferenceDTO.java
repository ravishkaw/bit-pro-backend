package com.ravi.waterlilly.payload.billingDiscount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// discount reference dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountReferenceDTO {
    private Integer id;
    private String code;
}