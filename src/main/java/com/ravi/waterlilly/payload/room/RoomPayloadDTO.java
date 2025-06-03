package com.ravi.waterlilly.payload.room;

import com.ravi.waterlilly.payload.inventory.InventoryQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

// payload dto for forms etc.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPayloadDTO {
    private Long id;
    private Integer number;
    private Integer adultNo;
    private Integer childNo;
    private Integer infantNo;
    private Integer floorNumber;
    private String photo;
    private BigDecimal price;
    private String description;
    private Integer statusId;
    private Integer roomTypeId;
    private Set<Integer> roomFacilityIds;
    private List<InventoryQuantityDTO> inventoryQuantities;
}
