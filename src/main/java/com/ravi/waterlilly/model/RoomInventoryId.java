package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Room has inventory many to many relationship id
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomInventoryId {

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "inventory_id")
    private Long inventoryId;
}