package com.ravi.waterlilly.payload.roomType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

// room type form dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypePayloadDTO {
    private Integer id;
    private String name;
    private BigDecimal basePrice;
    private String description;
    private Integer bedTypeId;
    private String statusName;
    private Set<Integer> pricingRuleIds;
}
