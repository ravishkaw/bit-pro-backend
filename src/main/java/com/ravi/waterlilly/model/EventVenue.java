package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// event venue entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_venue")
public class EventVenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "description")
    private String description;

    @Column(name = "photo")
    private String photo;

    @ManyToOne
    @JoinColumn(name = "event_venue_status_id")
    private EventVenueStatus status;
}
