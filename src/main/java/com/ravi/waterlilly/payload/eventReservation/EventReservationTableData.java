package com.ravi.waterlilly.payload.eventReservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// table data for event reservation
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventReservationTableData {
    private Long id;
    private String guestFullName;
    private String eventTypeName;
    private String name;
    private String eventVenueName;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private Integer expectedGuestCount;
    private String note;
    private LocalDateTime reservationDatetime;
    private String eventStatusName;
    private BigDecimal totalPrice;
    private String paymentStatusName;
}
