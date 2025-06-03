package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Room reservation has amenity many to many relationship entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_reservation_has_room_reservation_amenity")
public class RoomReservationHasAmenity {

    @EmbeddedId
    private RoomReservationHasAmenityId id;

    @ManyToOne
    @MapsId("roomReservationId")
    @JoinColumn(name = "room_reservation_id")
    private RoomReservation roomReservation;

    @ManyToOne
    @MapsId("roomReservationAmenityId")
    @JoinColumn(name = "room_reservation_amenity_id")
    private RoomReservationAmenity roomReservationAmenity;

    @Column(name = "quantity")
    private Integer quantity;
}
