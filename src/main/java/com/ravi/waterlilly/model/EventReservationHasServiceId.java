package com.ravi.waterlilly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Event reservation has service many to many relationship id
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventReservationHasServiceId {

    @Column(name = "event_reservation_id")
    private Long eventReservationId;

    @Column(name = "event_service_id")
    private Integer eventServiceId;
}
