package com.ravi.waterlilly.payload.roomReservation;

import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;
import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

// room reservation payload form dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationPayloadFormDTO {
    private Long id;
    private Long roomId;
    private Long primaryGuestId;
    private LocalDate reservedCheckInDate;
    private LocalDate reservedCheckOutDate;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Integer adultNo;
    private Integer childNo;
    private Integer infantNo;
    private String note;
    private Integer reservationTypeId;
    private Integer roomPackageId;
    private Integer roomReservationStatusId;
    private Integer roomReservationSourceId;
    private Set<Long> guestIds;
    private Set<Long> childIds;
    private BillingPayloadDTO billingPayloadDTO;
    private List<AmenityQuantityDTO> amenities;
}