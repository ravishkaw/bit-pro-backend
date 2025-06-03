package com.ravi.waterlilly.payload.eventReservation;

import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;
import com.ravi.waterlilly.payload.eventService.EventServiceQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

// Event payload dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventReservationPayloadFormDTO {
    private Long id;
    private Long guestId;
    private Integer eventTypeId;
    private String name;
    private Long eventVenueId;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private Integer expectedGuestCount;
    private String note;
    private Integer eventStatusId;
    private Integer eventPackageId;
    private BillingPayloadDTO billingPayloadDTO;
    private Set<EventServiceQuantityDTO> services;
}
