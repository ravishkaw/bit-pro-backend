package com.ravi.waterlilly.payload.roomReservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// table data for room reservation
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationTableData {
    private Long id;
    private Integer roomNumber;
    private String primaryGuestFullName;
    private LocalDateTime reservedCheckInDate;
    private LocalDateTime reservedCheckOutDate;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Integer adultNo;
    private Integer childNo;
    private Integer infantNo;
    private String reservationTypeName;
    private String roomReservationStatusName;
    private BigDecimal totalPrice;
    private String paymentStatusName;
}
