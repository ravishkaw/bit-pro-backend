package com.ravi.waterlilly.payload.eventReservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// page response for room reservation
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventReservationPageResponse {
    List<EventReservationTableData> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
