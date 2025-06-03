package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// room reservation entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_reservation")
public class RoomReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "primary_guest_id", referencedColumnName = "id")
    private Guest primaryGuest;

    @Column(name = "reserved_check_in_date")
    private LocalDateTime reservedCheckInDate;

    @Column(name = "reserved_check_out_date")
    private LocalDateTime reservedCheckOutDate;

    @Column(name = "check_in_date")
    private LocalDateTime checkInDate;

    @Column(name = "check_out_date")
    private LocalDateTime checkOutDate;

    @Column(name = "adult_no")
    private Integer adultNo;

    @Column(name = "child_no")
    private Integer childNo;

    @Column(name = "infant_no")
    private Integer infantNo;

    @Column(name = "note")
    private String note;

    @Column(name = "added_datetime")
    private LocalDateTime addedDatetime;

    @Column(name = "last_modified_datetime")
    private LocalDateTime lastModifiedDatetime;

    @ManyToOne
    @JoinColumn(name = "reservation_type_id")
    private RoomReservationType roomReservationType;

    @ManyToOne
    @JoinColumn(name = "room_reservation_status_id")
    private RoomReservationStatus roomReservationStatus;

    @ManyToOne
    @JoinColumn(name = "room_reservation_source_id")
    private RoomReservationSource roomReservationSource;

    @ManyToMany
    @JoinTable(name = "guest_has_room_reservation",
            joinColumns = @JoinColumn(name = "room_reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id"))
    private Set<Guest> guests = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "child_has_room_reservation",
            joinColumns = @JoinColumn(name = "room_reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    private Set<Child> children = new HashSet<>();

    @OneToMany(mappedBy = "roomReservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomReservationHasAmenity> reservationHasAmenities = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "room_package_id", referencedColumnName = "id")
    private RoomPackage roomPackage;

    @OneToOne
    @JoinColumn(name = "billing_id")
    private Billing billing;

    public void addAmenity(RoomReservationAmenity amenity, Integer quantity) {
        RoomReservationHasAmenityId id = new RoomReservationHasAmenityId();
        RoomReservationHasAmenity reservationAmenity = new RoomReservationHasAmenity();
        id.setRoomReservationId(this.id);
        id.setRoomReservationAmenityId(amenity.getId());

        reservationAmenity.setId(id);
        reservationAmenity.setRoomReservation(this);
        reservationAmenity.setRoomReservationAmenity(amenity);
        reservationAmenity.setQuantity(quantity);

        this.reservationHasAmenities.add(reservationAmenity);
    }

    public void clearAmenities() {
        this.reservationHasAmenities.clear();
    }
}