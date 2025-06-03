package com.ravi.waterlilly.payload.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// inventory table data
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryTableDataDTO {
    private Long id;
    private String itemName;
    private Integer quantity;
    private BigDecimal price;
    private LocalDate lastRestockedDate;
    private String itemTypeName;
    private String statusName;
}
