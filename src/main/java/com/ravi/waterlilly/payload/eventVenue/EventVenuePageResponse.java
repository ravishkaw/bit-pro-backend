package com.ravi.waterlilly.payload.eventVenue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// dto for event venue page response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventVenuePageResponse {
    private List<EventVenuePayloadDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
