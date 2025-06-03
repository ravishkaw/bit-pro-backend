package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// room package entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_package_has_room_reservation_amenity")
public class RoomPackageAmenity {

    @EmbeddedId
    private RoomPackageAmenityId id;

    @ManyToOne
    @MapsId("roomPackageId")
    @JoinColumn(name = "room_package_id")
    private RoomPackage roomPackage;

    @ManyToOne
    @MapsId("roomReservationAmenityId")
    @JoinColumn(name = "room_reservation_amenity_id")
    private RoomReservationAmenity roomReservationAmenity;

    private Integer quantity;
}
