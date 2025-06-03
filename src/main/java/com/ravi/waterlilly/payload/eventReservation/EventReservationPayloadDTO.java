package com.ravi.waterlilly.payload.eventReservation;

import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;
import com.ravi.waterlilly.payload.eventService.EventServiceQuantityDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

// Event payload dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventReservationPayloadDTO {
    private Long id;
    private Long guestId;
    private Integer eventTypeId;
    private String name;
    private Integer eventVenueId;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private Integer expectedGuestCount;
    private String note;
    private LocalDateTime reservationDatetime;
    private Integer eventStatusId;
    private Integer eventPackageId;
    private Set<Billing> billing;
    private Set<EventServiceQuantityDTO> services;
}
