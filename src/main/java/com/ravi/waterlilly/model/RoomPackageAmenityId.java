package com.ravi.waterlilly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Room Package Amenities Id for many to many relationship
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPackageAmenityId {

    @Column(name = "room_package_id")
    private Integer roomPackageId;

    @Column(name = "room_reservation_amenity_id")
    private Integer roomReservationAmenityId;
}
