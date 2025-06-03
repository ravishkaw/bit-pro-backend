package com.ravi.waterlilly.payload.roomPricingRule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO for pricing room table response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPricingRulePageResponse {
    private List<RoomPricingRulePayloadDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
