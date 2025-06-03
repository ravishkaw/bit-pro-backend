package com.ravi.waterlilly.payload.pdfGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItem {
    private String description;
    private String details;
    private BigDecimal amount;
    private LineItemType type;

    // Line item type enum
    public enum LineItemType {
        CHARGE, FEE, PENALTY, DISCOUNT, REFUND, TAX
    }

}
