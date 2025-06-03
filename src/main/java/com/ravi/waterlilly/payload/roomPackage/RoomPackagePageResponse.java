package com.ravi.waterlilly.payload.roomPackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Page response dto of room package
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPackagePageResponse {
    private List<RoomPackageBasicDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
