package com.ravi.waterlilly.payload.roomInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO for room inventory table response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomInventoryPageResponse {
    private List<RoomInventoryTableDataDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
