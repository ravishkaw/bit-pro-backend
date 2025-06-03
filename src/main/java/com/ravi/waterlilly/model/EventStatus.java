package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Event status class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_status")
public class EventStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;
}
