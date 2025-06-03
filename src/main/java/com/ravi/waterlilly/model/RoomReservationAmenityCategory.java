package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// room reservation amenities category ( meal, service...)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_reservation_amenity_category")
public class RoomReservationAmenityCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int Id;

    @NotBlank
    @Column(name = "name")
    private String name;
}
