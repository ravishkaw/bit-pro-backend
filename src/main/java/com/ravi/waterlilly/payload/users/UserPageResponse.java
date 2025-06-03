package com.ravi.waterlilly.payload.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO for user table response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageResponse {
    private List<UserTableDataDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
