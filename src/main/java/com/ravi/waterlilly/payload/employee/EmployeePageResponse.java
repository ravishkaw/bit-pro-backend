package com.ravi.waterlilly.payload.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Paginated employee response with employee table data
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePageResponse {
    private List<EmployeeTableDataDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
