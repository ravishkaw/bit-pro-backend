package com.ravi.waterlilly.payload.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// page response for task
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskPageResponse {
    List<TaskTableData> data;
    Integer pageNumber;
    Integer pageSize;
    Long totalElements;
    Integer totalPages;
    boolean lastPage;
}
