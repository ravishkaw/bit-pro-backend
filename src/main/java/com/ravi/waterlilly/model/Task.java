package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

// Task entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "target_type_id", referencedColumnName = "id")
    private TaskTargetType targetType;

    @Column(name = "target_id")
    private Long targetId;

    @ManyToOne
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    private Employee assignedTo;

    @Column(name = "description")
    private String description;

    @Column(name = "scheduled_start_time")
    private LocalDateTime scheduledStartTime;

    @Column(name = "scheduled_end_time")
    private LocalDateTime scheduledEndTime;

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    @Column(name = "added_datetime")
    private LocalDateTime addedDateTime;

    @Column(name = "last_modified_datetime")
    private LocalDateTime lastModifiedDateTime;

    @ManyToOne
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @ManyToOne
    @JoinColumn(name = "task_type_id")
    private TaskType taskType;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskInventory> taskInventory;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskCost> taskCost;

    public void addInventory(Inventory inventory, Integer quantity) {
        TaskInventory taskInventory = new TaskInventory();
        TaskInventoryId id = new TaskInventoryId();
        id.setTaskId(this.getId());
        id.setInventoryId(inventory.getId());
        taskInventory.setId(id);
        taskInventory.setTask(this);
        taskInventory.setInventory(inventory);
        taskInventory.setQuantity(quantity);
    }

    public void clearInventory() {
        this.taskInventory.clear();
    }

    public void addCost(TaskCost cost) {
        TaskCost taskCost = new TaskCost();
        taskCost.setTask(this);
        taskCost.setCostType(cost.getCostType());
        taskCost.setAmount(cost.getAmount());
    }

    public void clearCost() {
        this.taskCost.clear();
    }
}