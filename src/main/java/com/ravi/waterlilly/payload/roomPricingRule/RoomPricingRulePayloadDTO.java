package com.ravi.waterlilly.payload.roomPricingRule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// pricing rule dto payload
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPricingRulePayloadDTO {
    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal pricingMultiplier;
    private String statusName;
}