package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Task has inventory
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_has_inventory")
public class TaskInventory {

    @EmbeddedId
    private TaskInventoryId id;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @MapsId("inventoryId")
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Column(name = "quantity")
    private Integer quantity;
}
