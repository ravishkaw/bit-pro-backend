package com.ravi.waterlilly.payload.eventPackage;

import com.ravi.waterlilly.payload.eventService.EventServiceQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// payload of event package
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPackagePayloadDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String statusName;
    private List<EventServiceQuantityDTO> eventServices;
}
