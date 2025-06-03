package com.ravi.waterlilly.payload.eventPackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// dto for event package amenities
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPackageServiceNameDTO {
    private String serviceName;
    private Integer quantity;
}
