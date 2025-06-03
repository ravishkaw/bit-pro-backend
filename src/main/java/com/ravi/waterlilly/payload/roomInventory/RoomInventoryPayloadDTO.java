package com.ravi.waterlilly.payload.roomInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// dto of room inventory
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomInventoryPayloadDTO {
    private Integer id;
    private String operation;
    private Integer quantity;
    private LocalDate lastCheckedDate;
    private Long inventoryId;
    private Long roomId;
}
