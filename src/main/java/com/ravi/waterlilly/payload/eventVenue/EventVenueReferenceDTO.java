package com.ravi.waterlilly.payload.eventVenue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// dto for event venue payload
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventVenueReferenceDTO {
    private Integer id;
    private String name;
    private Integer capacity;
}
