package com.ravi.waterlilly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Task Inventory Id for many to many relationship
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInventoryId {

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "inventory_id")
    private Long inventoryId;
}
