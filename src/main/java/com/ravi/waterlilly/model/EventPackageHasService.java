package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Event Package Service class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_package_has_event_service")
public class EventPackageHasService {

    @EmbeddedId
    private EventPackageHasServiceId id;

    @ManyToOne
    @MapsId("eventPackageId")
    @JoinColumn(name = "event_package_id")
    private EventPackage eventPackage;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "event_service_id")
    private EventService eventService;

    @Column(name = "quantity")
    private Integer quantity;
}
