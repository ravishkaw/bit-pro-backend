package com.ravi.waterlilly.payload.eventService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// dto for pagination
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventServicePageResponse {
    List<EventServicePayloadDTO> data;
    Integer pageNumber;
    Integer pageSize;
    Long totalElements;
    Integer totalPages;
    boolean lastPage;
}
