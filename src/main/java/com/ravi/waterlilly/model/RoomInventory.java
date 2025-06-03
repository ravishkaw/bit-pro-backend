package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Room has inventory class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_has_inventory")
public class RoomInventory {

    @EmbeddedId
    private RoomInventoryId id;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @MapsId("inventoryId")
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Column(name = "quantity")
    private Integer quantity;
}