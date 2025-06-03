package com.ravi.waterlilly.payload.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// dto for inventory with quantity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryQuantityDTO {
    private Long inventoryId;
    private Integer quantity;
}