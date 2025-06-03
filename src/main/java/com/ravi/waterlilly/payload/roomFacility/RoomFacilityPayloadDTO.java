package com.ravi.waterlilly.payload.roomFacility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Dto of room facility payload
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomFacilityPayloadDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String statusName;
}
