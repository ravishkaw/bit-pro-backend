package com.ravi.waterlilly.payload.roomFacility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO for pricing room table response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomFacilityTableResponse {
    private List<RoomFacilityPayloadDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
