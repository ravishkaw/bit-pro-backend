package com.ravi.waterlilly.payload.privilege;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO for privilege table response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegePageResponse {
    private List<PrivilegeDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
