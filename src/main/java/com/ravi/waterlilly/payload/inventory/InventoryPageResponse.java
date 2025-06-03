package com.ravi.waterlilly.payload.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// dto to send data to table with pagination
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryPageResponse {
    private List<InventoryTableDataDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
