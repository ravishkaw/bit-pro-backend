package com.ravi.waterlilly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// room reservation has amenity id for many to many relationship
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationHasAmenityId {

    @Column(name = "room_reservation_id")
    private Long roomReservationId;

    @Column(name = "room_reservation_amenity_id")
    private Integer roomReservationAmenityId;
}
