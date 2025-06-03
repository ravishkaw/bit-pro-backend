package com.ravi.waterlilly.payload.roomReservationAmenity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// dto of room reservation amenities
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationAmenityPayloadDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer categoryId;
    private String statusName;
}
