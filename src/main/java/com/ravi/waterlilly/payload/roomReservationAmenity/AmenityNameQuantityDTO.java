package com.ravi.waterlilly.payload.roomReservationAmenity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// dto for room amenities with name and quantity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityNameQuantityDTO {
    private String amenityName;
    private Integer quantity;
}
