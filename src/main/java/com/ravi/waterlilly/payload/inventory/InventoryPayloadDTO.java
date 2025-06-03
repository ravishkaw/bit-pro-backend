package com.ravi.waterlilly.payload.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// simple dto of inventory entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryPayloadDTO {
    private Long id;
    private String itemName;
    private Integer quantity;
    private BigDecimal price;
    private LocalDate lastRestockedDate;
    private Integer itemTypeId;
    private Integer statusId;
}
