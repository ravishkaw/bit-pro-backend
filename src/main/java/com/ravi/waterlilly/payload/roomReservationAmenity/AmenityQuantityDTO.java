package com.ravi.waterlilly.payload.roomReservationAmenity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// dto for amenity with quantity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityQuantityDTO {
    private Integer amenityId;
    private Integer quantity;
}