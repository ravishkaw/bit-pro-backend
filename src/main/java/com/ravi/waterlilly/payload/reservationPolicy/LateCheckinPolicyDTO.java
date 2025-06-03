package com.ravi.waterlilly.payload.reservationPolicy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// late checkin policy class
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LateCheckinPolicyDTO {
    private Integer id;
    private Integer lateHoursThreshold;
    private BigDecimal lateCheckinFee;
}