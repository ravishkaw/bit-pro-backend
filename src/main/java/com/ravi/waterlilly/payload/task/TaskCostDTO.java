package com.ravi.waterlilly.payload.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// dto for task cost
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCostDTO {
    private String costType;
    private BigDecimal amount;
}
