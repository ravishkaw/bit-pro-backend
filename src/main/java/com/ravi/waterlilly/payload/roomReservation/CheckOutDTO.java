package com.ravi.waterlilly.payload.roomReservation;

import com.ravi.waterlilly.payload.billing.BillingPayloadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

// dto to checkout
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutDTO {
    private LocalDateTime checkOutDate;
    private BillingPayloadDTO billingPayloadDTO;
}
