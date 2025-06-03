package com.ravi.waterlilly.payload.roomInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// simple dto for room inventory
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomInventoryTableDataDTO {
    private Integer id;
    private Integer quantity;
    private LocalDate lastCheckedDate;
    private String inventoryItemName;
    private Integer roomNumber;
}
