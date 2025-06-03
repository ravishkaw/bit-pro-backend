package com.ravi.waterlilly.payload.billing;

import com.ravi.waterlilly.model.Billing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// refund dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundPayloadDTO {
    private Integer id;
    private Billing billing;
    private BigDecimal amount;
    private String reason;
    private LocalDateTime refundDatetime;
}
