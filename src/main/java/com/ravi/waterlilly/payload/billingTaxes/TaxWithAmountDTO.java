package com.ravi.waterlilly.payload.billingTaxes;

import com.ravi.waterlilly.model.Tax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// discount with amount dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxWithAmountDTO {
    private Tax tax;
    private BigDecimal amount;
}
