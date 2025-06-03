package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// entity of room reservation status ( reserved, check in,...)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_reservation_status")
public class RoomReservationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int Id;

    @NotBlank
    @Column(name = "name")
    private String name;
}