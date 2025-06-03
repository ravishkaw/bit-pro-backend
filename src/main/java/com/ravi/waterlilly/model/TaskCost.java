package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Task Cost entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_cost")
public class TaskCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "cost_type")
    private String costType;

    @Column(name = "amount")
    private BigDecimal amount;
}
