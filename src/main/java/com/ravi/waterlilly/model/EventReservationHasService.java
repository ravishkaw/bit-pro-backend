package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Event reservation has service class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_reservation_has_event_service")
public class EventReservationHasService {

    @EmbeddedId
    private EventReservationHasServiceId id;

    @ManyToOne
    @MapsId("eventReservationId")
    @JoinColumn(name = "event_reservation_id")
    private EventReservation eventReservation;

    @ManyToOne
    @MapsId("eventServiceId")
    @JoinColumn(name = "event_service_id")
    private EventService eventServices;

    @Column(name = "quantity")
    private Integer quantity;
}
