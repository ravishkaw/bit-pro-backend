package com.ravi.waterlilly.payload.roomType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// room type dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypePageResponse {
    private List<RoomTypeBasicDTO> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
