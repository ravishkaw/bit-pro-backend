package com.ravi.waterlilly.payload.eventPackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

// basic dto of event package
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPackageBasicDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String statusName;
    private Set<EventPackageServiceNameDTO> services;
}
