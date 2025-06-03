package com.ravi.waterlilly.payload.roomReservationBilling;

import com.ravi.waterlilly.payload.billingDiscount.DiscountReferenceDTO;
import com.ravi.waterlilly.payload.billingTaxes.TaxReferenceDTO;
import com.ravi.waterlilly.payload.roomReservationAmenity.AmenityNameQuantityDTO;
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
public class RoomReservationBillingDTO {
    private Integer roomNumber;
    private BigDecimal basePrice;
    private DiscountReferenceDTO discount;
    private BigDecimal discountAmount;
    private TaxReferenceDTO tax;
    private BigDecimal totalTaxes;
    private BigDecimal totalPrice;
    private BigDecimal netAmount;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private String roomPackageName;
    private List<AmenityNameQuantityDTO> amenities;
}
