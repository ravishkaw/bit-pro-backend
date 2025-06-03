package com.ravi.waterlilly.payload.billingTaxes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// tax reference dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxReferenceDTO {
    private Integer id;
    private String name;
    private BigDecimal percentage;
}