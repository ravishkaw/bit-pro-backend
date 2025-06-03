package com.ravi.waterlilly.payload.eventService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for event service quantity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventServiceQuantityDTO {
    private Integer eventServiceId;
    private Integer quantity;
}
