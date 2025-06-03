package com.ravi.waterlilly.payload.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Paginated Guest response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestPageResponse {
    private List<GuestTableDataDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
