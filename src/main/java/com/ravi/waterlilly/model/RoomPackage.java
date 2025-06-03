package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

// room package entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_package")
public class RoomPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @OneToMany(mappedBy = "roomPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomPackageAmenity> roomPackageAmenities;

    public void addAmenity(RoomReservationAmenity amenity, Integer quantity) {
        RoomPackageAmenity packageAmenity = new RoomPackageAmenity();
        RoomPackageAmenityId id = new RoomPackageAmenityId();
        id.setRoomPackageId(this.getId());
        id.setRoomReservationAmenityId(amenity.getId());

        packageAmenity.setId(id);
        packageAmenity.setRoomPackage(this);
        packageAmenity.setRoomReservationAmenity(amenity);
        packageAmenity.setQuantity(quantity);

        this.roomPackageAmenities.add(packageAmenity);
    }

    public void clearAmenities() {
        this.roomPackageAmenities.clear();
    }

}
