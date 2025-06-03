package com.ravi.waterlilly.payload.billingTaxes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// tax dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxPayloadDTO {
    private Integer id;
    private String name;
    private BigDecimal percentage;
    private String description;
    private String statusName;
}