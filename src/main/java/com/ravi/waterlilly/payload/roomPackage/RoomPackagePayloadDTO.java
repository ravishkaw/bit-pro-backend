package com.ravi.waterlilly.payload.roomPackage;

import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// payload of room package
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPackagePayloadDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String statusName;
    private List<AmenityQuantityDTO> amenities;
}
