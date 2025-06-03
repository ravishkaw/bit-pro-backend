package com.ravi.waterlilly.payload.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Billing Table Page Response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingPageResponse {
    private List<BillingTableData> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
