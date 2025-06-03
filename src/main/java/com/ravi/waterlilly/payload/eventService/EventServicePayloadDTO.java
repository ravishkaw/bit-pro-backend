package com.ravi.waterlilly.payload.eventService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// dto for event service payload
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventServicePayloadDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal pricePerUnit;
    private String statusName;
}
