package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//task target type class ( e.g., room, event venue)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_target_type")
public class TaskTargetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name", unique = true)
    private String name;
}
