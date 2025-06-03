package com.ravi.waterlilly.payload.roomPackage;

import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityNameQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

// basic dto of room package
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPackageBasicDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String statusName;
    private Set<AmenityNameQuantityDTO> amenities;
}
