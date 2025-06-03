package com.ravi.waterlilly.payload.roomReservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// page response for room reservation
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationPageResponse {
    List<RoomReservationTableData> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
