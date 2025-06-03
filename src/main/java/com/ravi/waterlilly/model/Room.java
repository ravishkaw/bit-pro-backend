package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//Room entity class
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "adult_no")
    private Integer adultNo;

    @Column(name = "child_no")
    private Integer childNo;

    @Column(name = "infant_no")
    private Integer infantNo;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "photo")
    private String photo;

    @Column(name = "description")
    private String description;

    @Column(name = "added_datetime")
    private LocalDateTime addedDatetime;

    @Column(name = "last_modified_datetime")
    private LocalDateTime lastModifiedDatetime;

    @Column(name = "deleted_datetime")
    private LocalDateTime deletedDatetime;

    @ManyToOne
    @JoinColumn(name = "room_status_id", referencedColumnName = "id")
    private RoomStatus status;

    @ManyToOne
    @JoinColumn(name = "room_type_id", referencedColumnName = "id")
    private RoomType roomType;

    @OneToMany(mappedBy = "room", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<RoomInventory> inventory = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "room_has_facility",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "room_facility_id"))
    private Set<RoomFacility> roomFacility;

    public void addInventory(Inventory inventory, Integer quantity) {
        RoomInventory roomInventory = new RoomInventory();
        RoomInventoryId id = new RoomInventoryId();
        id.setRoomId(this.getId());
        id.setInventoryId(inventory.getId());

        roomInventory.setId(id);
        roomInventory.setRoom(this);
        roomInventory.setInventory(inventory);
        roomInventory.setQuantity(quantity);

        this.inventory.add(roomInventory);
    }

    public void clearInventory() {
        this.inventory.clear();
    }
}
