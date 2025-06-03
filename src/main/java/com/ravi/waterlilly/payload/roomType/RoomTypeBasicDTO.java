package com.ravi.waterlilly.payload.roomType;

import com.ravi.waterlilly.payload.roomPricingRule.RoomPricingRulePayloadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

// room type dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeBasicDTO {
    private Integer id;
    private String name;
    private BigDecimal basePrice;
    private String description;
    private String bedTypeName;
    private String statusName;
    private Set<RoomPricingRulePayloadDTO> pricingRules;
}
