package com.ravi.waterlilly.payload.roomReservationBilling;

import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// DTO for room reservation billing
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationBillingPayloadDTO {
    private Long roomId;
    private BigDecimal basePrice;
    private BigDecimal discount;
    private BigDecimal totalPrice;
    private BigDecimal totalTaxes;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Integer roomPackageId;
    private List<AmenityQuantityDTO> amenities;
}
