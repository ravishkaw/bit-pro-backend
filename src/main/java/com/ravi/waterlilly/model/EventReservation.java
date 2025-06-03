package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Event class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_reservation")
public class EventReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "event_venue_id")
    private EventVenue eventVenue;

    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "expected_guest_count")
    private Integer expectedGuestCount;

    @Column(name = "note")
    private String note;

    @Column(name = "reservation_datetime")
    private LocalDateTime reservationDatetime;

    @ManyToOne
    @JoinColumn(name = "event_status_id")
    private EventStatus eventStatus;

    @ManyToOne
    @JoinColumn(name = "event_package_id")
    private EventPackage eventPackage;

    @OneToOne
    @JoinColumn(name = "billing_id")
    private Billing billing;

    @OneToMany(mappedBy = "eventReservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventReservationHasService> reservationServices = new HashSet<>();

    public void addReservationService(EventService service, Integer quantity) {
        EventReservationHasServiceId id = new EventReservationHasServiceId();
        EventReservationHasService eventReservationHasService = new EventReservationHasService();
        id.setEventReservationId(this.id);
        id.setEventServiceId(service.getId());

        eventReservationHasService.setId(id);
        eventReservationHasService.setEventReservation(this);
        eventReservationHasService.setEventServices(service);
        eventReservationHasService.setQuantity(quantity);

        this.reservationServices.add(eventReservationHasService);
    }

    public void clearReservationServices() {
        this.reservationServices.clear();
    }
}
