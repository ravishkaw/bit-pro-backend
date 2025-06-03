package com.ravi.waterlilly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Event Package Service Id for many to many relationship
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPackageHasServiceId {

    @Column(name = "event_package_id")
    private Integer eventPackageId;

    @Column(name = "event_service_id")
    private Integer serviceId;
}
